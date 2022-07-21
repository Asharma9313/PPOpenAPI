package com.pulsepoint.hcp365;

import com.pulsepoint.hcp365.enums.ReportArchivationType;
import com.pulsepoint.hcp365.enums.ReportFileType;
import com.pulsepoint.hcp365.modal.ReportFormatSetting;
import com.pulsepoint.hcp365.service.ReportFormatSettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ReportFormatSettingServiceTest {

    @Autowired
    ReportFormatSettingService reportFormatSettingService;

    @Test
    public void testGetReportFormatSetting(){
        ReportFormatSetting setting = reportFormatSettingService.findByAccountIdAndAdvId(559145L, 543L);
        assertNotNull(setting);
        assertEquals(1L, setting.getId());
    }

    @Test
    @Transactional
    public void testCreateReportFormatSetting(){
        ReportFormatSetting setting = getNewReportFormatSetting();
        setting = reportFormatSettingService.save(setting);
        assertNotNull(setting.getId());
    }

    private ReportFormatSetting getNewReportFormatSetting(){
        ReportFormatSetting setting = new ReportFormatSetting();
        setting.setAccountId(559146L);
        setting.setAdvertiserId(245L);
        setting.setArchivationType(ReportArchivationType.BZIP);
        setting.setColumnHeader(true);
        setting.setFileType(ReportFileType.CSV);
        setting.setUserId(4566L);
        return setting;
    }

}
