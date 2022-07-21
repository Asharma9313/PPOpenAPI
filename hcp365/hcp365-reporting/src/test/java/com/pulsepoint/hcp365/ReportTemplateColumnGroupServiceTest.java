package com.pulsepoint.hcp365;

import com.pulsepoint.hcp365.modal.ReportTemplateColumnGroup;
import com.pulsepoint.hcp365.service.ReportTemplateColumnGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ReportTemplateColumnGroupServiceTest {

    @Autowired
    ReportTemplateColumnGroupService service;

    @Test
    public void testGetAllColumnGroups() {
        List<ReportTemplateColumnGroup> groups = service.getAll();

        assertThat(groups, hasSize(2));
        ReportTemplateColumnGroup group = groups.get(0);
        assertThat(group.getOrdinal(), is(equalTo(1)));
        assertThat(group.getReportTemplateColumnList(), hasSize(2));
        assertThat(group.getReportTemplateColumnList().get(0).getOrdinal(), is(equalTo(1)));

        assertEquals(2, groups.size());
        assertEquals(1, group.getOrdinal());
        assertEquals(2, group.getReportTemplateColumnList().size());
        assertEquals(1, group.getReportTemplateColumnList().get(0).getOrdinal());
    }
}
