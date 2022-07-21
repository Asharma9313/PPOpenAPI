package com.pulsepoint.hcp365.dto;

import com.pulsepoint.hcp365.enums.ReportAttributeType;
import com.pulsepoint.hcp365.enums.ReportDownloadType;
import com.pulsepoint.hcp365.enums.ReportStatus;
import com.pulsepoint.hcp365.modal.*;
import com.pulsepoint.hcp365.service.ReportUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class EntityConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReportUtil reportUtil;

    public <F, T> T convertToDto(Class<T> clazz, F fromObject) {
        if (fromObject == null) {
            return null;
        }
        return modelMapper.map(fromObject, clazz);
    }

    public <F, T> List<T> convertToDtoList(Class<T> clazz, List<F> fromObjects) {
        List<T> dtos = new ArrayList<>();
        for (F fromObject : fromObjects) {
            if (nonNull(fromObject))
                dtos.add(convertToDto(clazz, fromObject));
        }
        return dtos;
    }

    public <F, T> T convertFromDto(Class<T> clazz, F fromObject) {
        if (fromObject == null) {
            return null;
        }
        return modelMapper.map(fromObject, clazz);
    }

    public <T> void mergeChanges(final T source, final T destination) {
        modelMapper.map(source, destination);
    }

    public ReportTemplate convertFromReportTemplateDTO(ReportTemplateDTO reportTemplateDTO) {
        if (reportTemplateDTO == null) {
            return null;
        }
        ReportTemplate reportTemplate = convertFromDto(ReportTemplate.class, reportTemplateDTO);
        if (CollectionUtils.isEmpty(reportTemplateDTO.getReportTemplateColumnDefinitionDTOS()) == false) {
            reportTemplate.setColumnDefinitionList(new ArrayList<>());
            reportTemplateDTO.getReportTemplateColumnDefinitionDTOS().stream().forEach(reportTemplateColumnDefinitionDTO -> {
                ReportTemplateColumnDefinition columnDefinition = convertFromDto(ReportTemplateColumnDefinition.class, reportTemplateColumnDefinitionDTO);
                columnDefinition.setTemplate(reportTemplate);
                columnDefinition.setTemplateColumn(null);
                reportTemplate.getColumnDefinitionList().add(columnDefinition);
            });
        }
        return reportTemplate;
    }

    public ReportTemplateDTO convertToReportTemplateDTO(ReportTemplate reportTemplate) {
        if (reportTemplate == null) {
            return null;
        }
        ReportTemplateDTO reportTemplateDTO = convertToDto(ReportTemplateDTO.class, reportTemplate);
        if (CollectionUtils.isEmpty(reportTemplate.getColumnDefinitionList()) == false) {
            reportTemplateDTO.setReportTemplateColumnDefinitionDTOS(new ArrayList<>());
            reportTemplate.getColumnDefinitionList().stream().forEach(reportTemplateColumnDefinition -> {
                reportTemplateDTO.getReportTemplateColumnDefinitionDTOS().add(convertToDto(ReportTemplateColumnDefinitionDTO.class, reportTemplateColumnDefinition));
            });
        }
        return reportTemplateDTO;
    }

    public ReportTemplate mergeReportTemplateChanges(ReportTemplate dbReportTemplate, ReportTemplate uiReportTemplate) {
        dbReportTemplate.getColumnDefinitionList().stream().forEach(dbColumnDef -> {
            ReportTemplateColumnDefinition uiColumnDef = uiReportTemplate.getColumnDefinitionList().stream().filter(c -> c.getId() != null && c.getId().equals(dbColumnDef.getId())).findFirst().orElse(null);
            if (uiColumnDef == null) {
                dbColumnDef.setStatus(false);
            } else {
                dbColumnDef.setCustomFieldName(uiColumnDef.getCustomFieldName());
                dbColumnDef.setFieldRefId(uiColumnDef.getFieldRefId());
                dbColumnDef.setTemplateColumn(null);
                dbColumnDef.setOrdinal(uiColumnDef.getOrdinal());
                dbColumnDef.setFieldType(uiColumnDef.getFieldType());
            }
        });
        uiReportTemplate.getColumnDefinitionList().stream().forEach(uiColumnDef -> {
            if (uiColumnDef.getId() == null) {
                dbReportTemplate.getColumnDefinitionList().add(uiColumnDef);
            }
        });
        return dbReportTemplate;
    }

    public ReportFormatSetting mergeReportFormatSetting(ReportFormatSetting dbSetting, ReportFormatSetting uiSetting) {
        dbSetting.setFileType(uiSetting.getFileType());
        dbSetting.setColumnHeader(uiSetting.isColumnHeader());
        dbSetting.setArchivationType(uiSetting.getArchivationType());
        dbSetting.setFileCustomDelimiter(uiSetting.getFileCustomDelimiter());
        dbSetting.setFileCustomExtension(uiSetting.getFileCustomExtension());
        return dbSetting;
    }

    public ScheduledReport mergeScheduledReports(ScheduledReport dbScheduledReport, ScheduledReport uiScheduledReport) {
        dbScheduledReport.setStatus(uiScheduledReport.isStatus());
        dbScheduledReport.setName(uiScheduledReport.getName());
        dbScheduledReport.setFrequencyId(uiScheduledReport.getFrequencyId());
        dbScheduledReport.setFileName(uiScheduledReport.getFileName());
        dbScheduledReport.setFilePath(uiScheduledReport.getFilePath());
        dbScheduledReport.setCustomDestinationId(uiScheduledReport.getCustomDestinationId());
        dbScheduledReport.setScheduleStartDate(uiScheduledReport.getScheduleStartDate());
        dbScheduledReport.setScheduleEndDate(uiScheduledReport.getScheduleEndDate());
        dbScheduledReport.setGenerateReportOnDayId(uiScheduledReport.getGenerateReportOnDayId());
        dbScheduledReport.setGenerateReportOnTime(uiScheduledReport.getGenerateReportOnTime());
        dbScheduledReport.setPeriodId(uiScheduledReport.getPeriodId());
        dbScheduledReport.setPeriodicalNumberType(uiScheduledReport.getPeriodicalNumberType());
        dbScheduledReport.setPeriodicalNumber(uiScheduledReport.getPeriodicalNumber());
        dbScheduledReport.setTimezoneId(uiScheduledReport.getTimezoneId());
        return dbScheduledReport;
    }

    public ReportSearchResultsDTO convertToReportSearchResultsDTO(Page<ReportLog> reportLogPage, Map<Long, String> placementMap) {
        ReportSearchResultsDTO searchResultsDTO = new ReportSearchResultsDTO();
        searchResultsDTO.setReportDetails(new ArrayList<>());
        searchResultsDTO.setTotal(reportLogPage.getTotalElements());
        if (CollectionUtils.isEmpty(reportLogPage.getContent()) == false) {
            reportLogPage.getContent().stream().forEach(reportLog -> {
                setCollectionDetails(placementMap, reportLog);
                ReportDetailDTO reportDetailDTO = new ReportDetailDTO();
                reportDetailDTO.setId(reportLog.getReportId());
                reportDetailDTO.setRequestId(reportLog.getRequestId());
                reportDetailDTO.setFileName(reportUtil.getReportFileName(reportLog));
                reportDetailDTO.setStatus(reportLog.getStatus());
                StringBuilder processDetails = new StringBuilder();
                if (reportLog.getDownloadType().equals(ReportDownloadType.CUSTOMDESTINATION)) {
                    if (reportLog.getScheduleId() != null) {
                        processDetails.append("Scheduled, ");
                    }
                    if (reportLog.getStatus().equals(ReportStatus.Success)) {
                        if (reportLog.getScheduleId() != null) {
                            processDetails.append("sent on ");
                        } else {
                            processDetails.append("Sent on ");
                        }
                    } else if (reportLog.getStatus().equals(ReportStatus.Fail)) {
                        if (reportLog.getScheduleId() != null) {
                            processDetails.append("failed on ");
                        } else {
                            processDetails.append("Failed on ");
                        }
                    } else {
                        if (reportLog.getScheduleId() != null) {
                            processDetails.append("run on ");
                        } else {
                            processDetails.append("Run on ");
                        }
                    }
                } else {
                    processDetails.append("Run on ");
                }
                LocalDate lastModifiedDate = reportLog.getLastModified().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                processDetails.append(lastModifiedDate.toString());
                reportDetailDTO.setProcessDescription(processDetails.toString());
                reportDetailDTO.setCollections(this.convertToDtoList(LookupDTO.class, reportLog.getCollections()));
                searchResultsDTO.getReportDetails().add(reportDetailDTO);
            });
        }
        return searchResultsDTO;
    }

    private void setCollectionDetails(Map<Long, String> placementMap, ReportLog reportLog) {
        List<Lookup> collectionIds = reportLog.getReportDefinitions().stream()
                .filter(attribute -> attribute.getReportAttributeTypeId() == ReportAttributeType.COLLECTIONID.ordinal())
                .map(scheduleReportDefinition -> new Lookup(Long.valueOf(scheduleReportDefinition.getAttributeValue()), placementMap.get(Long.valueOf(scheduleReportDefinition.getAttributeValue())), null))
                .collect(Collectors.toList());
        reportLog.setCollections(collectionIds);
    }

    public ScheduleReportCollectionDTO convertToScheduleReportSearchResultsDTO(Page<ScheduledReport> scheduledReports, Map<Long, String> placementMap) {
        ScheduleReportCollectionDTO searchResultsDTO = new ScheduleReportCollectionDTO();
        searchResultsDTO.setTotal(scheduledReports.getTotalElements());
        if (CollectionUtils.isEmpty(scheduledReports.getContent()) == false) {
            scheduledReports.getContent().forEach(scheduledReport -> {
                setCollectionDetails(placementMap, scheduledReport);
            });
            searchResultsDTO.setReportDetails(convertToDtoList(ScheduledReportDTO.class, scheduledReports.getContent()));
        }
        return searchResultsDTO;
    }

    private void setCollectionDetails(Map<Long, String> placementMap, ScheduledReport scheduledReport) {
        List<Lookup> collectionIds = scheduledReport.getScheduleReportDefinitions().stream()
                .filter(attribute -> attribute.getReportAttributeTypeId() == ReportAttributeType.COLLECTIONID.ordinal())
                .map(scheduleReportDefinition -> new Lookup(Long.valueOf(scheduleReportDefinition.getAttributeValue()), placementMap.get(Long.valueOf(scheduleReportDefinition.getAttributeValue())), null))
                .collect(Collectors.toList());
        scheduledReport.setCollections(collectionIds);
    }
}
