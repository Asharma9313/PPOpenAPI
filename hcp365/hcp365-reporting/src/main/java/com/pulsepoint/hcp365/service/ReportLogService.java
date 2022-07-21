package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.enums.ReportAttributeType;
import com.pulsepoint.hcp365.enums.ReportDownloadType;
import com.pulsepoint.hcp365.enums.ReportStatus;
import com.pulsepoint.hcp365.modal.ReportDefinition;
import com.pulsepoint.hcp365.modal.ReportLog;
import com.pulsepoint.hcp365.modal.ReportTemplate;
import com.pulsepoint.hcp365.repository.ReportLogRepository;
import com.pulsepoint.hcp365.repository.ReportTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.naming.CommunicationException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportLogService {
    private static final Logger logger = LoggerFactory.getLogger(ReportLogService.class);
    public static final int recordSize = 5;
    @Autowired
    ReportLogRepository reportLogRepository;

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;

    @Autowired
    private ReportingAPIIntegrationService reportingAPIIntegrationService;

    @Autowired
    private ReportLogSeparateTransactionalService reportLogSeparateTransactionalService;

    @Transactional
    public ReportLog createNew(ReportLogParam param) {
        ReportLog reportLog = generateNewReportLog(param);
        //reportLog = reportLogRepository.save(reportLog);
        reportLog = reportLogSeparateTransactionalService.saveReportLog(reportLog);
        ReportTemplate template = reportTemplateRepository.findByAccountIdAndAdvertiserIdAndStatus(param.getAccountId(), param.getAdvId(), true);
        if (template == null) {
            template = reportTemplateRepository.findByAccountIdAndAdvertiserIdAndStatus(-1L, -1L, true);
        }
        try {
            reportingAPIIntegrationService.callReportingAPI(reportLog, template);
        } catch (RuntimeException ex) {
            logger.error("Error while calling reporting api", ex);
        }
        return reportLog;
    }

    public ReportLog getById(Long id) {
        return reportLogRepository.getById(id);
    }

    public ReportLog getByRequestId(String requestId) {
        return reportLogRepository.getByRequestId(requestId);
    }

    public ReportLog update(ReportLog reportLog) {
        return reportLogRepository.save(reportLog);
    }

    public ReportLog generateNewReportLog(ReportLogParam param) {
        ReportLog reportLog = new ReportLog();
        reportLog.setAccountId(param.getAccountId());
        reportLog.setArchive(false);
        reportLog.setRequestId(UUID.randomUUID().toString());
        reportLog.setUserId(param.getUserId());
        reportLog.setStatus(ReportStatus.Pending);
        reportLog.setArchive(false);
        if (param.getCustomDestinationId() != null) {
            reportLog.setDownloadType(ReportDownloadType.CUSTOMDESTINATION);
            reportLog.setFileName(param.getFileName());
            reportLog.setFilePath(param.getFilePath());
            reportLog.setCustomDestinationId(param.getCustomDestinationId());
        } else {
            reportLog.setDownloadType(ReportDownloadType.DIRECTDOWNLOAD);
        }
        reportLog.setReportDefinitions(new ArrayList<>());
        reportLog.getReportDefinitions().add(new ReportDefinition(null, reportLog, null, ReportAttributeType.FROMDATE.ordinal(), param.getFromDate(), true));
        reportLog.getReportDefinitions().add(new ReportDefinition(null, reportLog, null, ReportAttributeType.TODATE.ordinal(), param.getToDate(), true));
        reportLog.getReportDefinitions().add(new ReportDefinition(null, reportLog, null, ReportAttributeType.ADVID.ordinal(), param.getAdvId().toString(), true));
        if (CollectionUtils.isEmpty(param.getCollectionIds()) == false) {
            param.getCollectionIds().stream().forEach(collectionId -> {
                reportLog.getReportDefinitions().add(new ReportDefinition(null, reportLog, null, ReportAttributeType.COLLECTIONID.ordinal(), collectionId.toString(), true));
            });
        }
        return reportLog;
    }

    public Page<ReportLog> searchReportLogs(Long accountId, Long advId, List<Long> collectionIds, int pageNo) {
        Sort sort = Sort.by("createdOn").descending();
        List<String> collectionIdStrings = collectionIds.stream().map(id -> id.toString()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collectionIdStrings)){
            return Page.empty();
        }
        Specification<ReportLog> specification = (Specification<ReportLog>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("accountId"), accountId));
            predicates.add(builder.equal(root.get("archive"), false));
            query.multiselect(root.get("reportId")).distinct(true);
            Join<ReportLog, ReportDefinition> defJoin = root.join("reportDefinitions");
            predicates.add(builder.equal(defJoin.get("reportAttributeTypeId"), recordSize));
            Expression<Long> collectionIdsExpression = defJoin.get("attributeValue");
            if (!collectionIdStrings.isEmpty()) {
                Predicate collectionIdsPredicate = collectionIdsExpression.in(collectionIdStrings);
                predicates.add(collectionIdsPredicate);
            }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Pageable paging = PageRequest.of(pageNo, 5, sort);
        return reportLogRepository.findAll(specification, paging);
    }
}
