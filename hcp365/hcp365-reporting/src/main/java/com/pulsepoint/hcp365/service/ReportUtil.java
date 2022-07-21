package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.enums.ReportAttributeType;
import com.pulsepoint.hcp365.modal.ReportLog;
import com.pulsepoint.hcp365.repository.ReportFormatSettingRepository;
import com.pulsepoint.hcp365.repository.ReportLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


@Service
public class ReportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class);
    private static final String ACCOUNTNAME = "$AccountName$";
    private static final String ACCOUNTID = "$AccountId$";
    private static final String ADVERTISERID = "$AdvertiserId$";
    private static final String ADVERTISERNAME = "$AdvertiserName$";
    private static final String COLLECTIONNAME = "$CollectionName$";
    private static final String PERIODSTART = "$PeriodStart[yyyy-MM-dd]$";
    private static final String PERIODEND = "$PeriodEnd[yyyy-MM-dd]$";
    private static final String REPORTDATE = "$ReportDate[yyyy-MM-dd]$";

    @Autowired
    private ReportLogRepository reportLogRepository;

    @Autowired
    private ReportFormatSettingRepository settingRepository;

    public String replaceFileVariablesWithValues(String fileVariable, String periodStart, String periodEnd, String accountName, String accountId, String advId, String advName) {
        if (fileVariable.indexOf(ACCOUNTNAME) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ACCOUNTNAME), accountName);
        }
        if (fileVariable.indexOf(ACCOUNTID) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ACCOUNTID), accountId);
        }
        if (fileVariable.indexOf(ADVERTISERID) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ADVERTISERID), advId);
        }
        if (fileVariable.indexOf(ADVERTISERNAME) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(ADVERTISERNAME), advName);
        }
        if (fileVariable.indexOf(PERIODSTART) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(PERIODSTART), formatDate(periodStart));
        }
        if (fileVariable.indexOf(PERIODEND) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(PERIODEND), formatDate(periodEnd));
        }
        if (fileVariable.indexOf(REPORTDATE) > -1) {
            fileVariable = fileVariable.replaceAll(java.util.regex.Pattern.quote(REPORTDATE), "T" + LocalDate.now().toString());
        }
        return fileVariable;
    }


    private String formatDate(String dbDate) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return "T" + apiDateFormat.format(format.parse(dbDate));
        } catch (Exception ex) {
            logger.error("Error occurred while formating report date {} for custom destination", dbDate, ex);
            throw new RuntimeException("Error occurred while parsing date: " + dbDate, ex);
        }
    }

    public String getReportFileName(ReportLog reportLog) {
        String fileName = "";
        Long placementId = reportLog.getReportDefinitions().stream().filter(d -> d.getReportAttributeTypeId() == ReportAttributeType.COLLECTIONID.ordinal()).map(a -> Long.parseLong(a.getAttributeValue())).findFirst().get();
        Long advId = reportLog.getReportDefinitions().stream().filter(d -> d.getReportAttributeTypeId() == ReportAttributeType.ADVID.ordinal()).map(a -> Long.parseLong(a.getAttributeValue())).findFirst().get();
        String startDate = reportLog.getReportDefinitions().stream().filter(d -> d.getReportAttributeTypeId() == ReportAttributeType.FROMDATE.ordinal()).map(a -> a.getAttributeValue()).findFirst().get();
        String endDate = reportLog.getReportDefinitions().stream().filter(d -> d.getReportAttributeTypeId() == ReportAttributeType.TODATE.ordinal()).map(a -> a.getAttributeValue()).findFirst().get();
        List<Object[]> variableValues = reportLogRepository.getReportVariableValues(placementId, reportLog.getAccountId());
        if (reportLog.getFileName() == null) {
            fileName = "Report from " + formatReportDate(startDate) + " to " + formatReportDate(endDate);
        } else {
            fileName = replaceFileVariablesWithValues(reportLog.getFileName(), startDate, endDate, variableValues.get(0)[2].toString(),
                    reportLog.getAccountId().toString(), advId.toString(), variableValues.get(0)[1].toString());
            fileName = fileName + "  from " + formatReportDate(startDate) + " to " + formatReportDate(endDate);
        }
        /*fileName = fileName + ".";
        ReportFormatSetting setting = settingRepository.findByAccountIdAndAdvertiserId(reportLog.getAccountId(), advId);
        if (Objects.nonNull(setting)) {
            if (setting.getFileType().equals(ReportFileType.CUSTOM)) {
                fileName = fileName + setting.getFileCustomExtension();
            } else {
                fileName = fileName + setting.getFileType().name().toLowerCase();
            }
        }*/
        return fileName;
    }

    private String formatReportDate(String date) {
        String formatedDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            formatedDate = apiDateFormat.format(format.parse(date));

        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while parsing date");
        }
        return formatedDate;

    }
}
