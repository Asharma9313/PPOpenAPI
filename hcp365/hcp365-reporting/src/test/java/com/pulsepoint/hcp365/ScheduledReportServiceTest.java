package com.pulsepoint.hcp365;

import com.pulsepoint.hcp365.enums.ReportPeriodicalType;
import com.pulsepoint.hcp365.enums.ScheduledReportFrequencyType;
import com.pulsepoint.hcp365.modal.ScheduledReport;
import com.pulsepoint.hcp365.service.ScheduledReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ScheduledReportServiceTest {

    @Autowired
    ScheduledReportService service;

    @Test
    public void testGetScheduledReport(){
        ScheduledReport scheduledReport = service.getActiveSchedule(559145L, 243L);
        assertNotNull(scheduledReport.getId());
        assertEquals(1L, scheduledReport.getId());
    }

    @Test
    @Transactional
    public void testCreateNewScheduledReport(){
        ScheduledReport scheduledReport = getNewScheduledReport();
        scheduledReport = service.saveScheduledReport(scheduledReport);
        assertNotNull(scheduledReport.getId());
    }

    private ScheduledReport getNewScheduledReport(){
        ScheduledReport scheduledReport = new ScheduledReport();
        scheduledReport.setAccountId(559146L);
        scheduledReport.setAdvId(246L);
        scheduledReport.setCustomDestinationId(2L);
        scheduledReport.setFilePath("${accountId}/${accountName}");
        scheduledReport.setFileName("test");
        scheduledReport.setFrequencyId(ScheduledReportFrequencyType.DAILY);
        scheduledReport.setStatus(true);
        scheduledReport.setScheduleStartDate(new Date());
        scheduledReport.setScheduleEndDate(new Date());
        scheduledReport.setGenerateReportOnDayId(1);
        scheduledReport.setGenerateReportOnTime("6:00");
        scheduledReport.setPeriodId(1);
        scheduledReport.setPeriodicalNumber(1);
        scheduledReport.setPeriodicalNumberType(ReportPeriodicalType.DAY);
        scheduledReport.setTimezoneId(1);
        return scheduledReport;
    }
}
