package com.pulsepoint.hcp365.service;

import com.pulsepoint.commons.utils.Util;
import com.pulsepoint.hcp365.enums.ReportAttributeType;
import com.pulsepoint.hcp365.enums.ReportStatus;
import com.pulsepoint.hcp365.modal.*;
import com.pulsepoint.hcp365.repository.ReportTemplateRepository;
import com.pulsepoint.hcp365.repository.ScheduledReportRepository;
import com.pulsepoint.hcp365.repository.ScheduledReportsQueueRepository;
import com.pulsepoint.hcp365.repository.TimeZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RefreshScope
public class ScheduledReportService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledReportService.class);

    @Value("${scheduledReportsQueueProcessCount}")
    private int scheduledReportsQueueProcessCount;

    private static final String ACCOUNTNAME = "$AccountName$";
    private static final String ACCOUNTID = "$AccountId$";
    private static final String ADVERTISERID = "$AdvertiserId$";
    private static final String ADVERTISERNAME = "$AdvertiserName$";
    private static final String COLLECTIONNAME = "$CollectionName$";
    private static final String PERIODSTART = "$PeriodStart[yyyy-MM-dd]$";
    private static final String PERIODEND = "$PeriodEnd[yyyy-MM-dd]$";
    private static final String REPORTDATE = "$ReportDate[yyyy-MM-dd]$";

    @Autowired
    private ScheduledReportRepository scheduledReportRepository;

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Autowired
    private ScheduledReportsQueueRepository queueRepository;

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;

    @Autowired
    private ReportLogService reportLogService;

    @Autowired
    private ReportDatesService reportDatesService;

    @Autowired
    private ReportLogSeparateTransactionalService reportLogTransactionalService;

    @Autowired
    private ReportingAPIIntegrationService reportingAPIIntegrationService;

    @Autowired
    private ScheduledReportQueueTransactionalService queueTransactionalService;

    @Autowired
    private ReportUtil reportUtil;

    public ScheduledReport getActiveSchedule(Long accountId, Long advId) {
        return scheduledReportRepository.findByAccountIdAndAdvIdAndStatus(accountId, advId, true);
    }

    public ScheduledReport getSchedule(Long id) {
        return scheduledReportRepository.findById(id).get();
    }

    public ScheduledReport getScheduleId(Long id) {
        return scheduledReportRepository.findById(id).get();
    }

    public ScheduledReport saveScheduledReport(ScheduledReport scheduledReport) {
        return scheduledReportRepository.save(scheduledReport);
    }

    public void generateScheduleReportsQueue() {
        List<com.pulsepoint.hcp365.modal.TimeZone> timeZones = timeZoneRepository.findAll();
        List<Object[]> activeSchedules = scheduledReportRepository.getReportsScheduledForToday();
        List<ScheduledReportsQueue> scheduledReportsQueue = new ArrayList<>();
        if (CollectionUtils.isEmpty(activeSchedules) == false) {
            activeSchedules.parallelStream().forEach(schedule -> {
                ScheduledReportsQueue scheduledReportsQueueItem = new ScheduledReportsQueue();
                scheduledReportsQueueItem.setScheduleId(Long.parseLong(schedule[0].toString()));
                scheduledReportsQueueItem.setProcessed(false);
                java.util.TimeZone clientTimeZone = java.util.TimeZone.getTimeZone(timeZones.parallelStream().filter(t -> t.getTimeZoneId().equals(Long.parseLong(schedule[3].toString()))).findFirst().get().getTimeZoneCode());
                try {
                    scheduledReportsQueueItem.setScheduleDate(Util.convertToESTDate((schedule[1].toString() + " " + schedule[2].toString()), clientTimeZone));
                    scheduledReportsQueueItem.setScheduleDateWithOutTimeZone((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)).parse(schedule[1].toString() + " " + schedule[2].toString()));
                } catch (ParseException e) {
                    logger.error("Error in Schedule reports date parsing", e);
                }
                scheduledReportsQueue.add(scheduledReportsQueueItem);
            });
            queueRepository.saveAll(scheduledReportsQueue);
        }
    }

    public void processScheduledReportsQueue() {
        List<Object[]> schedulesList = queueRepository.getScheduledReportsForProcessing(scheduledReportsQueueProcessCount);
        if (CollectionUtils.isEmpty(schedulesList) == false) {
            schedulesList.stream().forEach(schedule -> {
                ReportLog reportLog = null;
                try {

                    ScheduledReport scheduledReport = scheduledReportRepository.findById(Long.parseLong(schedule[0].toString())).get();
                    Long accountId = scheduledReport.getAccountId();
                    Long advId = scheduledReport.getAdvId();
                    ReportTemplate template = reportTemplateRepository.findByAccountIdAndAdvertiserIdAndStatus(accountId, advId, true);
                    if (Objects.isNull(template)) {
                        template = reportTemplateRepository.findByAccountIdAndAdvertiserIdAndStatus(-1L, -1L, true);
                    }
                    ReportLogParam reportLogParam = generateReportLog(scheduledReport, schedule);
                    reportLog = reportLogService.generateNewReportLog(reportLogParam);
                    reportLog.setScheduleId(Long.parseLong(schedule[0].toString()));
                    reportLog = reportLogTransactionalService.saveReportLog(reportLog);
                    reportingAPIIntegrationService.callReportingAPI(reportLog, template);
                    ScheduledReportsQueue reportFromQueue = queueRepository.findById(Long.parseLong(schedule[2].toString())).orElse(null);
                    reportFromQueue.setProcessed(true);
                    queueTransactionalService.update(reportFromQueue);

                } catch (Exception ex) {
                    logger.error("Error occurred in HCP365 schedule report for id " + schedule[0].toString(), ex);
                    updateReportLogStatus(reportLog);
                    //throw new RuntimeException("Error occurred in HCP365 schedule report for id " + schedule[0].toString(), ex);
                }
            });

        }
    }

    private void updateReportLogStatus(ReportLog reportLog) {
        reportLog.setStatus(ReportStatus.Fail);
        reportLogService.update(reportLog);
    }

    private ReportLogParam generateReportLog(ScheduledReport scheduledReport, Object[] scheduleData) throws ParseException {
        String[] startAndEndDates = reportDatesService.generateReportDates(scheduleData);
        ReportLogParam reportLogParam = new ReportLogParam();
        reportLogParam.setUserId(scheduledReport.getUserId());
        List<Long> collectionIds = getReportCollectionIds(scheduledReport);
        reportLogParam.setCollectionIds(collectionIds);
        reportLogParam.setFromDate(startAndEndDates[0]);
        reportLogParam.setToDate(startAndEndDates[1]);
        reportLogParam.setAccountId(scheduledReport.getAccountId());
        reportLogParam.setCustomDestinationId(scheduledReport.getCustomDestinationId());
        reportLogParam.setFileName(reportUtil.replaceFileVariablesWithValues(scheduledReport.getFileName(), startAndEndDates[0], startAndEndDates[1], scheduleData[9].toString(), scheduleData[7].toString(), scheduleData[8].toString(), scheduleData[10].toString()));
        setReportFilePath(scheduledReport, scheduleData, startAndEndDates, reportLogParam);
        reportLogParam.setAdvId(scheduledReport.getAdvId());
        return reportLogParam;
    }

    private void setReportFilePath(ScheduledReport scheduledReport, Object[] scheduleData, String[] startAndEndDates, ReportLogParam reportLogParam) {
        if (Objects.nonNull(scheduledReport.getFilePath())) {
            reportLogParam.setFilePath(reportUtil.replaceFileVariablesWithValues(scheduledReport.getFilePath(), startAndEndDates[0], startAndEndDates[1], scheduleData[9].toString(), scheduleData[7].toString(), scheduleData[8].toString(), scheduleData[10].toString()));
        } else {
            reportLogParam.setFilePath("");
        }
    }

    private List<Long> getReportCollectionIds(ScheduledReport scheduledReport) {
        return scheduledReport.getScheduleReportDefinitions().stream()
                .filter(attribute -> attribute.getReportAttributeTypeId() == ReportAttributeType.COLLECTIONID.ordinal())
                .map(def -> Long.valueOf(def.getAttributeValue()))
                .collect(Collectors.toList());
    }

    /*private String replaceFileVariablesWithValues(String fileVariable, Object[] scheduleData, String periodStart, String periodEnd){
        if(fileVariable.indexOf(ACCOUNTNAME) > -1){
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ACCOUNTNAME), scheduleData[10].toString());
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ACCOUNTID), scheduleData[7].toString());
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ADVERTISERID), scheduleData[8].toString());
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ADVERTISERNAME), scheduleData[11].toString());
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(COLLECTIONNAME), scheduleData[12].toString());
            if(fileVariable.indexOf(PERIODSTART ) > -1){
                fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(PERIODSTART), formatDate(periodStart));
            }
            if(fileVariable.indexOf(PERIODEND) > -1){
                fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(PERIODEND), formatDate(periodEnd));
            }
            if(fileVariable.indexOf(REPORTDATE) > -1){
                fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(REPORTDATE), "T" + LocalDate.now().toString());
            }
        }
        return fileVariable;
    }*/

    public Page<ScheduledReport> searchReportLogs(Long accountId, List<Long> collectionIds, int pageNo) {
        Sort sort = Sort.by("name").ascending();
        List<String> collectionIdStrings = collectionIds.stream().map(id -> id.toString()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collectionIdStrings)) {
            return Page.empty();
        }
        Specification<ScheduledReport> specification = (Specification<ScheduledReport>) (root, query, builder) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("accountId"), accountId));
            predicates.add(builder.isFalse(root.get("archive")));
            Join<ReportLog, ReportDefinition> defJoin = root.join("scheduleReportDefinitions");
            predicates.add(builder.equal(defJoin.get("reportAttributeTypeId"), 5));
            Expression<Long> collectionIdsExpression = defJoin.get("attributeValue");
            if (!collectionIdStrings.isEmpty()) {
                Predicate collectionIdsPredicate = collectionIdsExpression.in(collectionIdStrings);
                predicates.add(collectionIdsPredicate);
            }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Pageable paging = PageRequest.of(pageNo, 5, sort);
        return scheduledReportRepository.findAll(specification, paging);
    }


}
