package com.pulsepoint.hcp365;

import com.pulsepoint.hcp365.modal.ReportLog;
import com.pulsepoint.hcp365.service.ReportLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ReportLogServiceTest {
    @Autowired
    ReportLogService service;

    @Test
    @Transactional
    public void testGetReportLog() {
        ReportLog reportLog = service.getById(1L);
        assertNotNull(reportLog);
        assertThat(reportLog.getReportId(), is(equalTo(1L)));
        assertThat(reportLog.getReportDefinitions(), hasSize(4));
    }

    /*@Test
    @Transactional()
    public void testCreateReportLog() {
        ReportLogParam param = new ReportLogParam();
        param.setAccountId(559146L);
        param.setUserId(545L);
        param.setFromDate("2021-08-01");
        param.setToDate("2021-08-04");
        param.setAdvId(1L);
        param.setCollectionIds(new ArrayList<>());
        param.getCollectionIds().add(8L);
        param.getCollectionIds().add(9L);
        ReportLog reportLog = service.createNew(param);
        assertThat(reportLog.getReportId(), is(notNullValue()));
        assertThat(reportLog.getReportDefinitions().get(0).getId(), is(notNullValue()));
    }*/

    @Test
    public void searchReports() {
        List<Long> collectionIds = new ArrayList<>();
        collectionIds.add(1L);
        Page<ReportLog> reports = service.searchReportLogs(559145L, 243L, collectionIds, 0);
        assertThat(reports.getTotalElements(), is(equalTo(1L)));
    }
}
