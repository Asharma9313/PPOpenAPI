package com.pulsepoint.hcp365.service;

import com.pulsepoint.commons.utils.Util;
import com.pulsepoint.hcp365.enums.ReportArchivationType;
import com.pulsepoint.hcp365.enums.ReportAttributeType;
import com.pulsepoint.hcp365.enums.ReportFileType;
import com.pulsepoint.hcp365.enums.ReportTemplateFieldType;
import com.pulsepoint.hcp365.modal.*;
import com.pulsepoint.hcp365.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReportingAPIIntegrationService {

    public static final String REPORT_TYPE = "daily";
    private static final String DATA_SOURCE = "hcp_dashboard";
    private final String[] groupby = {"pixelId"};
    @Autowired
    ReportFormatSettingRepository formatSettingRepository;

    @Autowired
    ReportAttributeRefRepository reportAttributeRefRepository;

    @Autowired
    private ReportLogSeparateTransactionalService reportLogTransactionalService;

    @Autowired
    private BasicAdvertiserRepository advertiserRepository;

    @Autowired
    private BasicPlacementRepository placementRepository;

    @Value("${reportingAPIUrl}")
    private String reportingAPIUrl;


    @Value("${reportingAPICallBackUrl}")
    private String reportingAPICallBackUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ReportLogRepository reportLogRepository;

    @Autowired
    private ReportUtil reportUtil;

    @Value("${spring.profiles.active}")
    private String env;

    public void callReportingAPI(ReportLog reportLog, ReportTemplate reportTemplate) {
        ReportRequestV2 reportRequestV2 = generateReportRequest(reportLog, reportTemplate);
        try {
            reportLog.setQuery(Util.convertToJSON(reportRequestV2));
        } catch (Exception ex) {
            log.error("Error serializing report Query for ReportLog Id" + reportLog.getReportId(), ex);
        }
        reportLogTransactionalService.saveReportLog(reportLog);
        updateInReportingAPISystem(reportRequestV2);
    }

    private void updateInReportingAPISystem(ReportRequestV2 reportRequestV2) {
        try {
            RequestEntity<ReportRequestV2> request =
                    RequestEntity.post(new URI(reportingAPIUrl))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(reportRequestV2, ReportRequestV2.class);
            ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);
            log.debug(responseEntity.getBody());
        } catch (Exception ex) {
            log.error("Error occurred while calling reporting api", ex);
            throw new RuntimeException("Unable to call reporting api");
        }
    }

    private ReportRequestV2 generateReportRequest(ReportLog reportLog, ReportTemplate reportTemplate) {
        ReportRequestV2 reportRequest = new ReportRequestV2();
        reportRequest.setReport_id(reportLog.getReportId());
        reportRequest.setVersion(2);
        reportRequest.setSelect(generateReportColumns(reportTemplate));

        reportRequest.setWhere(generateWhereConditions(reportLog));
        ReportFormatSetting reportFormatSetting = formatSettingRepository.findByAccountIdAndAdvertiserId(reportLog.getAccountId(), reportTemplate.getAdvertiserId());
        if (reportFormatSetting == null) {
            reportFormatSetting = formatSettingRepository.findByAccountIdAndAdvertiserId(-1L, -1L);
        }
        reportRequest.setReport_type(REPORT_TYPE);
        reportRequest.setData_source(DATA_SOURCE);
        reportRequest.setResult_callback(reportingAPICallBackUrl.replace("{reportid}", reportLog.getReportId().toString()));
        setCompressionType(reportRequest, reportFormatSetting);
        if (reportFormatSetting.getFileType() == null) {
            reportRequest.setFile_format(ReportFileType.CSV.name().toLowerCase());
        } else {
            if (!equals(ReportFileType.CUSTOM)) {
                reportRequest.setFile_format(reportFormatSetting.getFileType().name().toLowerCase());
            } else {
                reportRequest.setFile_format(reportFormatSetting.getFileCustomExtension().toLowerCase());
            }
        }

        if (reportLog.getCustomDestinationId() != null) {
            ReportRequestDestination destination = new ReportRequestDestination();
            destination.setId(reportLog.getCustomDestinationId());
            ReportRequestDestinationControlFields controlFields = new ReportRequestDestinationControlFields();
            reportLog.getReportDefinitions().stream()
                    .filter(d -> d.getReportAttributeTypeId() == ReportAttributeType.ADVID.ordinal())
                    .map(a -> Long.parseLong(a.getAttributeValue()))
                    .findAny()
                    .flatMap(id -> advertiserRepository.findById(id))
                    .ifPresent(advertiser -> controlFields.setAdvertiser(advertiser.getName()));
            reportLog.getReportDefinitions().stream()
                    .filter(d -> d.getReportAttributeTypeId() == ReportAttributeType.COLLECTIONID.ordinal())
                    .map(a -> Long.parseLong(a.getAttributeValue()))
                    .findFirst()
                    .flatMap(id -> placementRepository.findById(id))
                    .ifPresent(placement -> controlFields.setPlacement(placement.getPlacementName()));
            destination.setControl_fields(controlFields);
            processReportDestination(reportLog, destination, controlFields);
            reportRequest.setDestination(destination);
        }
        reportRequest.setEnvironment("stage".equalsIgnoreCase(env) ? "PROD" : env);
        return reportRequest;
    }

    private void processReportDestination(ReportLog reportLog, ReportRequestDestination destination, ReportRequestDestinationControlFields controlFields) {
        Long placementId = Long.parseLong(searchReportDefinition(reportLog.getReportDefinitions(), ReportAttributeType.COLLECTIONID).get(0).getAttributeValue());
        List<Object[]> variableValues = reportLogRepository.getReportVariableValues(placementId, reportLog.getAccountId());
        String accountName = variableValues.get(0)[2].toString();
        String periodStart = searchReportDefinition(reportLog.getReportDefinitions(), ReportAttributeType.FROMDATE).get(0).getAttributeValue();
        String periodEnd = searchReportDefinition(reportLog.getReportDefinitions(), ReportAttributeType.TODATE).get(0).getAttributeValue();
        Long advId = Long.parseLong(searchReportDefinition(reportLog.getReportDefinitions(), ReportAttributeType.ADVID).get(0).getAttributeValue());
        String advertiserName = controlFields.getAdvertiser();
        destination.setFile_name(reportUtil.replaceFileVariablesWithValues(reportLog.getFileName(), periodStart, periodEnd, accountName, reportLog.getAccountId().toString(), advId.toString(), advertiserName));
        destination.setFile_path(reportUtil.replaceFileVariablesWithValues(reportLog.getFilePath(), periodStart, periodEnd, accountName, reportLog.getAccountId().toString(), advId.toString(), advertiserName));
    }

    private void setCompressionType(ReportRequestV2 reportRequest, ReportFormatSetting reportFormatSetting) {
        if (reportFormatSetting.getArchivationType().equals(ReportArchivationType.NONE)) {
            reportRequest.setCompression_type(null);
        } else {
            reportRequest.setCompression_type(reportFormatSetting.getArchivationType().name());
        }
    }

    private List<ReportDefinition> searchReportDefinition
            (List<ReportDefinition> reportDefinitions, ReportAttributeType attributeType) {
        return reportDefinitions.stream().filter(reportDefinition ->
                reportDefinition.getReportAttributeTypeId() == attributeType.ordinal()).collect(Collectors.toList());
    }


    private List<ReportRequestColumn> generateReportColumns(ReportTemplate reportTemplate) {
        List<ReportRequestColumn> selectedColumns = new ArrayList<>();
        List<ReportTemplateColumnDefinition> templateColumnDefinitions = reportTemplate.getColumnDefinitionList();
        templateColumnDefinitions.forEach(columnDefinition -> {
            if (columnDefinition.isStatus()) {
                if (columnDefinition.getFieldType().equals(ReportTemplateFieldType.PREDEFINED)) {
                    if (columnDefinition.getCustomFieldName() == null) {
                        selectedColumns.add(new ReportRequestColumn(columnDefinition.getTemplateColumn().getReportingAPIColName(), null, null));
                    } else {
                        selectedColumns.add(new ReportRequestColumn(columnDefinition.getTemplateColumn().getReportingAPIColName(), null, columnDefinition.getCustomFieldName()));
                    }
                } else if (columnDefinition.getFieldType().equals(ReportTemplateFieldType.CUSTOM)) {
                    selectedColumns.add(new ReportRequestColumn("BLANK COLUMN", null, columnDefinition.getCustomFieldName()));
                }
            }
        });
        return selectedColumns;
    }

    private String[] generateWhereConditions(ReportLog reportLog) {
        List<String> whereConditions = new ArrayList<>();
        List<CollectionPixelTokenData> placementTokenList = new ArrayList<>();
        List<String> placementTokens;
        List<ReportAttributeRef> allAttributes = reportAttributeRefRepository.findAll();
        List<Object[]> placementPixelTokens = placementRepository.getPlacementPixelTokens();

        List<String> collectionIds = reportLog.getReportDefinitions().stream().filter(attribute -> attribute.getReportAttributeTypeId() == ReportAttributeType.COLLECTIONID.ordinal())
                .map(ReportDefinition::getAttributeValue)
                .collect(Collectors.toList());

        List<CollectionPixelTokenData> collectionPixelTokenData = placementPixelTokens.stream()
                .map(pixel -> new CollectionPixelTokenData(Integer.valueOf(pixel[0].toString()), pixel[1].toString())).collect(Collectors.toList());

        Map<Integer, List<CollectionPixelTokenData>> collectionPixelTokenGroupMap = collectionPixelTokenData.stream().collect(Collectors.groupingBy(CollectionPixelTokenData::getId));

        collectionIds.stream().map(placement -> collectionPixelTokenGroupMap.get(Integer.valueOf(placement))).forEach(placementTokenList::addAll);

        placementTokens = placementTokenList.stream().map(CollectionPixelTokenData::getToken).collect(Collectors.toList());

        //generateFilterByType(reportLog, ReportAttributeType.ADVID, allAttributes, whereConditions);
        generateCollectionFilter(placementTokens, ReportAttributeType.COLLECTIONID, allAttributes, whereConditions);
        whereConditions.add(generateDateFilter(reportLog));
        whereConditions.add("is_hcp = 1");
        return whereConditions.toArray(new String[0]);
    }

    private String generateDateFilter(ReportLog exchangeReportLog) {
        String startDate = searchReportDefinition(exchangeReportLog.getReportDefinitions(), ReportAttributeType.FROMDATE).get(0).getAttributeValue();
        String endDate = searchReportDefinition(exchangeReportLog.getReportDefinitions(), ReportAttributeType.TODATE).get(0).getAttributeValue();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            startDate = apiDateFormat.format(format.parse(startDate));
            endDate = apiDateFormat.format((format.parse(endDate)));
        } catch (Exception ex) {
            log.error("Error occurred while parsing date", ex);
            throw new RuntimeException("Error occurred while parsing date");
        }

        return " Day >= '" + startDate + "' and Day <='" + endDate + "'";
    }

    private void generateCollectionFilter(List<String> placementTokens, ReportAttributeType
            attributeType, List<ReportAttributeRef> allAttributes, List<String> whereConditions) {
        ReportAttributeRef attribute = allAttributes.stream().filter(a -> a.getId().equals((long) attributeType.ordinal())).findFirst().orElse(null);
        if (!CollectionUtils.isEmpty(placementTokens) && Objects.nonNull(attribute)) {
            StringBuilder whereCondition = new StringBuilder("PIXELID");
            if (placementTokens.size() == 1) {
                whereCondition.append(" = ").append("'").append(placementTokens.get(0)).append("'");
            } else {
                whereCondition.append(placementTokens.stream().map(t -> "'" + t + "'").collect(Collectors.joining(",", " in (", " )")));
            }
            whereConditions.add(whereCondition.toString());
        }
    }

    private void generateFilterByType(ReportLog reportLog, ReportAttributeType
            attributeType, List<ReportAttributeRef> allAttributes, List<String> whereConditions) {
        List<ReportDefinition> reportDefinitionList = reportLog.getReportDefinitions().stream().filter(d -> d.getReportAttributeTypeId() == attributeType.ordinal()).collect(Collectors.toList());
        ReportAttributeRef attribute = allAttributes.stream().filter(a -> a.getId().equals((long) attributeType.ordinal())).findFirst().orElse(null);
        if (!CollectionUtils.isEmpty(reportDefinitionList) && attribute != null) {
            StringBuilder whereCondition = new StringBuilder(attribute.getReportingAPIColName());
            if (reportDefinitionList.size() == 1) {
                whereCondition.append(" = ").append(reportDefinitionList.get(0).getAttributeValue());
            } else {
                whereCondition.append(
                        reportDefinitionList.stream()
                                .map(ReportDefinition::getAttributeValue)
                                .collect(Collectors.joining(",", " in (", " )"))
                );
            }
            whereConditions.add(whereCondition.toString());
        }
    }
}