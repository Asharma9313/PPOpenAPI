package com.pulsepoint.hcp365.controller;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.dto.EntityConverter;
import com.pulsepoint.hcp365.dto.ReportFileDTO;
import com.pulsepoint.hcp365.dto.ReportSearchResultsDTO;
import com.pulsepoint.hcp365.dto.ReportStatusDTO;
import com.pulsepoint.hcp365.enums.ReportArchivationType;
import com.pulsepoint.hcp365.enums.ReportFileType;
import com.pulsepoint.hcp365.enums.ReportStatus;
import com.pulsepoint.hcp365.modal.*;
import com.pulsepoint.hcp365.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/reporting/report")
@RefreshScope
@Transactional
public class ReportLogController {

    public static final long DefaultAdvertiserId = -1L;
    public static final long DefaultAccountId = -1L;
    @Autowired
    ReportLogService reportLogService;

    @Autowired
    EntityConverter entityConverter;

    @Autowired
    ReportUtil reportUtil;

    @Autowired
    PlacementService placementService;

    @Autowired
    ReportTemplateService reportTemplateService;

    @Autowired
    ReportFormatSettingService reportFormatSettingService;

    @PostMapping("/")
    @Transactional(propagation = Propagation.NEVER)
    public void createNewReport(@RequestBody ReportLogParam reportLogParam) {
        createDefaultReportFormatIfNotExists(reportLogParam);
        createDefaultReportTemplateIfNotExists(reportLogParam);
        reportLogParam.setUserId(SecurityContextUtil.getUserId());
        reportLogParam.setAccountId(SecurityContextUtil.getAccountId());
        reportLogService.createNew(reportLogParam);

    }

    @GetMapping("/advertiser/{advId}/collection/{collectionId}/page/{pageNo}")
    public ReportSearchResultsDTO searchReports(@PathVariable("advId") Long advId, @PathVariable("collectionId") Long collectionId, @PathVariable("pageNo") int pageNo) {
        List<Long> collectionIds = getCollections(collectionId, advId);
        Page<ReportLog> reportLogPage = reportLogService.searchReportLogs(SecurityContextUtil.getAccountId(), advId, collectionIds, pageNo);
        List<BasicPlacement> placements = placementService.findAll();
        Map<Long, String> placementMap = placements.stream().collect(Collectors.toMap(BasicPlacement::getId, BasicPlacement::getPlacementName));
        return entityConverter.convertToReportSearchResultsDTO(reportLogPage, placementMap);
    }

    @PutMapping("/{id}/status")
    public void updateReportStatus(@RequestBody ReportStatusDTO reportStatusDTO, @PathVariable("id") Long reportId) {
        if (SecurityContextUtil.isSpecialUser() == true || SecurityContextUtil.isAdmin() == true) {
            ReportLog reportLog = reportLogService.getById(reportId);
            if (reportLog == null) {
                throw new InvalidDataException("Invalid report id");
            }
            reportLog.setStatus(ReportStatus.fromInt(reportStatusDTO.getStatus()));
            reportLog.setDownloadLink(reportStatusDTO.getDownloadLink());
            reportLog.setErrorMessage(reportStatusDTO.getErrorMessage());
            reportLogService.update(reportLog);
        }
    }

    @GetMapping("/request/{requestId}")
    public ReportFileDTO getReportFileDTO(@PathVariable("requestId") String requestId) {
        ReportLog reportLog = reportLogService.getByRequestId(requestId);
        ReportFileDTO reportFileDTO = new ReportFileDTO();
        reportFileDTO.setAccountId(reportLog.getAccountId());
        reportFileDTO.setFileLocation(reportLog.getDownloadLink());
        reportFileDTO.setUserFileName(reportUtil.getReportFileName(reportLog));
        return reportFileDTO;
    }

    @PutMapping("/archive/{reportId}")
    public void archiveSchedule(@PathVariable("reportId") Long reportId) {
        ReportLog reportLog = reportLogService.getById(reportId);
        reportLog.setArchive(true);
        reportLogService.update(reportLog);
    }

    private List<Long> getCollections(Long collectionId, Long advId) {
        List<Long> collectionIds = new ArrayList<>();
        if (collectionId > 0) {
            collectionIds.add(collectionId);
        } else {
            List<BasicPlacement> placements = placementService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), advId, true);
            collectionIds.addAll(placements.stream().map(BasicPlacement::getId).collect(Collectors.toList()));
        }
        return collectionIds;
    }

    private void createDefaultReportTemplateIfNotExists(ReportLogParam reportLogParam) {
        ReportTemplate reportTemplate = reportTemplateService.getReportTemplate(SecurityContextUtil.getAccountId(), reportLogParam.getAdvId());
        if (Objects.isNull(reportTemplate)) {
            ReportTemplate defaultReportTemplate = reportTemplateService.getReportTemplate(DefaultAccountId, DefaultAdvertiserId);
            ReportTemplate newReportTemplate = (ReportTemplate) defaultReportTemplate.clone();
            entityConverter.mergeChanges(defaultReportTemplate, newReportTemplate);
            newReportTemplate.setId(null);
            newReportTemplate.setAccountId(SecurityContextUtil.getAccountId());
            newReportTemplate.setAdvertiserId(reportLogParam.getAdvId());
            newReportTemplate.setUserId(SecurityContextUtil.getUserId());
            setColumnDefinitions(defaultReportTemplate, newReportTemplate);
            reportTemplateService.save(newReportTemplate);
        }
    }

    private void setColumnDefinitions(ReportTemplate defaultReportTemplate, ReportTemplate newReportTemplate) {
        newReportTemplate.getColumnDefinitionList().clear();
        defaultReportTemplate.getColumnDefinitionList().forEach(column -> {
            ReportTemplateColumnDefinition reportTemplateColumnDefinition = new ReportTemplateColumnDefinition();
            reportTemplateColumnDefinition.setFieldRefId(column.getFieldRefId());
            reportTemplateColumnDefinition.setStatus(true);
            reportTemplateColumnDefinition.setOrdinal(column.getOrdinal());
            reportTemplateColumnDefinition.setTemplateColumn(column.getTemplateColumn());
            reportTemplateColumnDefinition.setCustomFieldName(column.getCustomFieldName());
            reportTemplateColumnDefinition.setFieldType(column.getFieldType());
            newReportTemplate.getColumnDefinitionList().add(reportTemplateColumnDefinition);
            reportTemplateColumnDefinition.setTemplate(newReportTemplate);
        });
    }

    private void createDefaultReportFormatIfNotExists(ReportLogParam reportLogParam) {
        ReportFormatSetting reportFormatSetting = reportFormatSettingService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), reportLogParam.getAdvId());
        if (Objects.isNull(reportFormatSetting)) {
            createReportFormatSetting(reportLogParam);
        }
    }

    private void createReportFormatSetting(ReportLogParam reportLogParam) {
        ReportFormatSetting setting = new ReportFormatSetting();
        setting.setUserId(SecurityContextUtil.getUserId());
        setting.setAccountId(SecurityContextUtil.getAccountId());
        setting.setAdvertiserId(reportLogParam.getAdvId());
        setting.setFileType(ReportFileType.CSV);
        setting.setArchivationType(ReportArchivationType.NONE);
        reportFormatSettingService.save(setting);
    }

}
