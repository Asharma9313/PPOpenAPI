package com.pulsepoint.hcp365;

import com.pulsepoint.hcp365.enums.ReportTemplateFieldType;
import com.pulsepoint.hcp365.modal.ReportTemplate;
import com.pulsepoint.hcp365.modal.ReportTemplateColumnDefinition;
import com.pulsepoint.hcp365.service.ReportTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ReportTemplateServiceTest {

    @Autowired
    ReportTemplateService service;

    @Test
    public void getTemplate() {
        ReportTemplate reportTemplate = service.getReportTemplate(559879L, 526L);
        assertNotNull(reportTemplate);
        assertEquals(3L, reportTemplate.getId());
        assertEquals(2, reportTemplate.getColumnDefinitionList().size());
        assertEquals(1, reportTemplate.getColumnDefinitionList().get(0).getOrdinal());
    }

    @Test
    @Transactional
    //@org.springframework.transaction.annotation.Transactional
    public void testCreateNewTemplate() {
        ReportTemplate reportTemplate = service.save(getNewReportTemplate());
        assertTrue(reportTemplate.getId() > 0);
        assertTrue(reportTemplate.getColumnDefinitionList().get(0).getId() > 0);
    }

    @Test
    @Transactional
    //@org.springframework.transaction.annotation.Transactional
    public void updateTemplate() {
        ReportTemplate reportTemplate = service.getReportTemplate(559878L, 525L);
        ReportTemplateColumnDefinition columnDefinition = reportTemplate.getColumnDefinitionList().get(0);
        columnDefinition.setFieldRefId(null);
        columnDefinition.setCustomFieldName("Custom Name Test");
        reportTemplate.getColumnDefinitionList().add(new ReportTemplateColumnDefinition(null, reportTemplate, ReportTemplateFieldType.PREDEFINED, null, 2L, null, 3, true));
        service.save(reportTemplate);
        reportTemplate = service.getReportTemplate(559878L, 525L);
        assertEquals("Custom Name Test", reportTemplate.getColumnDefinitionList().get(0).getCustomFieldName());
        service.save(reportTemplate);
        assertNotNull(reportTemplate.getColumnDefinitionList().get(2).getId());
        reportTemplate.getColumnDefinitionList().get(0).setStatus(false);
        service.save(reportTemplate);
        reportTemplate = service.getReportTemplate(559878L, 525L);
        assertEquals(3, reportTemplate.getColumnDefinitionList().size());
    }


    private ReportTemplate getNewReportTemplate() {
        ReportTemplate reportTemplate = new ReportTemplate();
        reportTemplate.setAccountId(559256L);
        reportTemplate.setName("Test template 2");
        reportTemplate.setAdvertiserId(445L);
        reportTemplate.setStatus(true);
        reportTemplate.setUserId(4778L);
        reportTemplate.setColumnDefinitionList(new ArrayList<>());
        ;
        reportTemplate.getColumnDefinitionList().add(new ReportTemplateColumnDefinition(null, reportTemplate, ReportTemplateFieldType.PREDEFINED, null, 1L, null, 1, true));
        return reportTemplate;
    }

}
