package com.pulsepoint.hcp365.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.dto.EntityConverter;
import com.pulsepoint.hcp365.dto.ScheduleReportCollectionDTO;
import com.pulsepoint.hcp365.dto.ScheduledReportDTO;
import com.pulsepoint.hcp365.enums.ReportArchivationType;
import com.pulsepoint.hcp365.enums.ReportAttributeType;
import com.pulsepoint.hcp365.enums.ReportFileType;
import com.pulsepoint.hcp365.modal.*;
import com.pulsepoint.hcp365.service.PlacementService;
import com.pulsepoint.hcp365.service.ReportFormatSettingService;
import com.pulsepoint.hcp365.service.ReportTemplateService;
import com.pulsepoint.hcp365.service.ScheduledReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/reporting/schedule")
@RefreshScope
@Transactional
public class ScheduledReportController {

    public static final long DefaultAdvertiserId = -1L;
    public static final long DefaultAccountId = -1L;

    @Autowired
    ScheduledReportService service;

    @Autowired
    EntityConverter entityConverter;

    @Autowired
    PlacementService placementService;

    @Autowired
    ReportTemplateService reportTemplateService;

    @Autowired
    ReportFormatSettingService reportFormatSettingService;

    @GetMapping("/advertiser/{advId}")
    public ScheduledReportDTO getByAdvId(@PathVariable("advId") Long advId) {
        ScheduledReport scheduledReport = service.getActiveSchedule(SecurityContextUtil.getAccountId(), advId);
        return entityConverter.convertToDto(ScheduledReportDTO.class, scheduledReport);
    }

    @GetMapping("/advertiser/{advId}/collection/{collectionId}/page/{pageNo}")
    public ScheduleReportCollectionDTO searchScheduleReports(@PathVariable("advId") Long advId, @PathVariable("collectionId") Long collectionId, @PathVariable("pageNo") int pageNo) {
        List<Long> collectionIds = getCollections(collectionId, advId);
        Page<ScheduledReport> reportLogPage = service.searchReportLogs(SecurityContextUtil.getAccountId(), collectionIds, pageNo);
        List<BasicPlacement> placements = placementService.findAll();
        Map<Long, String> placementMap = placements.stream().collect(Collectors.toMap(BasicPlacement::getId, BasicPlacement::getPlacementName));
        return entityConverter.convertToScheduleReportSearchResultsDTO(reportLogPage, placementMap);
    }

    @PostMapping("/advertiser/{advId}")
    public ScheduledReportDTO createNewSchedule(@PathVariable("advId") Long advId, @RequestBody ScheduledReportDTO scheduledReportDTO) {
        createDefaultReportFormatIfNotExists(scheduledReportDTO);
        createDefaultReportTemplateIfNotExists(scheduledReportDTO);
        ScheduledReport scheduledReport = entityConverter.convertFromDto(ScheduledReport.class, scheduledReportDTO);
        setNewReportDefaults(scheduledReport);
        setCollections(scheduledReportDTO, scheduledReport);
        scheduledReport = service.saveScheduledReport(scheduledReport);
        return entityConverter.convertToDto(ScheduledReportDTO.class, scheduledReport);
    }

    @PutMapping("/advertiser/{advId}")
    public ScheduledReportDTO updateSchedule(@PathVariable("advId") Long advId, @RequestBody ScheduledReportDTO scheduledReportDTO) {
        return updateSchedule(scheduledReportDTO);
    }

    @PostMapping("/trigger/queue")
    public void triggerScheduledReportsQueue() {
        service.generateScheduleReportsQueue();
    }

    @PostMapping("/trigger/queue/process")
    @Transactional(propagation = Propagation.NEVER)
    public void processScheduledReportsQueue() {
        service.processScheduledReportsQueue();
    }

    private ScheduledReportDTO updateSchedule(ScheduledReportDTO scheduledReportDTO) {
        ScheduledReport dbScheduledReport = service.getSchedule(scheduledReportDTO.getId());
        ScheduledReport uiScheduledReport = entityConverter.convertFromDto(ScheduledReport.class, scheduledReportDTO);
        updateCollections(scheduledReportDTO, dbScheduledReport);
        dbScheduledReport = entityConverter.mergeScheduledReports(dbScheduledReport, uiScheduledReport);
        service.saveScheduledReport(dbScheduledReport);
        return scheduledReportDTO;
    }

    @PutMapping("/disable/{scheduleId}")
    public void disableSchedule(@PathVariable("scheduleId") Long scheduleId) {
        ScheduledReport scheduledReport = service.getScheduleId(scheduleId);
        scheduledReport.setStatus(false);
        service.saveScheduledReport(scheduledReport);
    }

    @PutMapping("/enable/{scheduleId}")
    public void enableSchedule(@PathVariable("scheduleId") Long scheduleId) {
        ScheduledReport scheduledReport = service.getScheduleId(scheduleId);
        scheduledReport.setStatus(true);
        service.saveScheduledReport(scheduledReport);
    }

    @PutMapping("/archive/{scheduleId}")
    public void archiveSchedule(@PathVariable("scheduleId") Long scheduleId) {
        ScheduledReport scheduledReport = service.getScheduleId(scheduleId);
        scheduledReport.setArchive(true);
        scheduledReport.setName(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        service.saveScheduledReport(scheduledReport);
    }

    private void updateCollections(ScheduledReportDTO scheduledReportDTO, ScheduledReport dbScheduledReport) {
        dbScheduledReport.getScheduleReportDefinitions().forEach(scheduleReportDefinition -> scheduleReportDefinition.setStatus(false));
        scheduledReportDTO.getCollectionIds().forEach(collection -> dbScheduledReport.getScheduleReportDefinitions().add(new ScheduleReportDefinition(null, dbScheduledReport, null, ReportAttributeType.COLLECTIONID.ordinal(), collection.toString(), true)));
    }

    private void setNewReportDefaults(ScheduledReport scheduledReport) {
        scheduledReport.setAccountId(SecurityContextUtil.getAccountId());
        scheduledReport.setUserId(SecurityContextUtil.getUserId());
        scheduledReport.setArchive(false);
    }

    private void setCollections(ScheduledReportDTO scheduledReportDTO, ScheduledReport scheduledReport) {
        if (!CollectionUtils.isEmpty(scheduledReportDTO.getCollectionIds())) {
            scheduledReportDTO.getCollectionIds().forEach(collection -> scheduledReport.getScheduleReportDefinitions().add(new ScheduleReportDefinition(null, scheduledReport, null, ReportAttributeType.COLLECTIONID.ordinal(), collection.toString(), true)));
        }
    }

    private List<Long> getCollections(Long collectionId, long advId) {
        List<Long> collectionIds = new ArrayList<>();
        if (collectionId > 0) {
            collectionIds.add(collectionId);
        } else {
            List<BasicPlacement> placements = placementService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), advId, true);
            collectionIds.addAll(placements.stream().map(BasicPlacement::getId).collect(Collectors.toList()));
        }
        return collectionIds;
    }

    private void createDefaultReportFormatIfNotExists(ScheduledReportDTO reportLogParam) {
        ReportFormatSetting reportFormatSetting = reportFormatSettingService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), reportLogParam.getAdvId());
        if (Objects.isNull(reportFormatSetting)) {
            createReportFormatSetting(reportLogParam);
        }
    }


    private void createDefaultReportTemplateIfNotExists(ScheduledReportDTO reportLogParam) {
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


    private void createReportFormatSetting(ScheduledReportDTO reportLogParam) {
        ReportFormatSetting setting = new ReportFormatSetting();
        setting.setUserId(SecurityContextUtil.getUserId());
        setting.setAccountId(SecurityContextUtil.getAccountId());
        setting.setAdvertiserId(reportLogParam.getAdvId());
        setting.setFileType(ReportFileType.CSV);
        setting.setArchivationType(ReportArchivationType.NONE);
        reportFormatSettingService.save(setting);
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
}
