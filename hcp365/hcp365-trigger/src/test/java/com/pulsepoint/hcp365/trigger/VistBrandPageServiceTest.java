package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageCollectionRef;
import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageSetting;
import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageURLFilterRef;
import com.pulsepoint.hcp365.trigger.service.VisitBrandPageSettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class VistBrandPageServiceTest {
    @Autowired
    VisitBrandPageSettingService service;

    @Test
    public void testGetVisitBrandPageSetting(){
        VisitBrandPageSetting visitBrandPageSetting = service.findByTriggerId(1L);
        assertThat(visitBrandPageSetting, is(notNullValue()));
        assertThat(visitBrandPageSetting.getTriggerId(), is(equalTo(1L)));
    }

    @Test
    @Transactional
    public void testGetVisitBrandPageSettingAndChildren(){
        VisitBrandPageSetting visitBrandPageSetting = service.findByTriggerId(2L);
        assertThat(visitBrandPageSetting, is(notNullValue()));
        assertThat(visitBrandPageSetting.getTriggerId(), is(equalTo(2L)));
        assertThat(visitBrandPageSetting.getVisitBrandPageCollectionRefs(), is(notNullValue()));
        assertThat(visitBrandPageSetting.getVisitBrandPageCollectionRefs().size(), is(equalTo(2)));
        assertThat(visitBrandPageSetting.getVisitBrandPageURLFilterRefs().size(), is(equalTo(2)));
    }

    @Test
    public void testCreateVisitBrandPageSetting(){
        VisitBrandPageSetting visitBrandPageSetting = new VisitBrandPageSetting();
        visitBrandPageSetting.setTriggerId(4L);
        visitBrandPageSetting.setCustomUrlParamName("Param1");
        visitBrandPageSetting.setFrequencyControlValue(8);
        visitBrandPageSetting.setCustomUrlParamValue("Custom Param Value");
        visitBrandPageSetting.setUrlFilterOperator(Operator.TARGET);
        List<VisitBrandPageCollectionRef> visitBrandPageCollectionRefs = new ArrayList<>();
        VisitBrandPageCollectionRef visitBrandPageCollectionRef1 = new VisitBrandPageCollectionRef();
        visitBrandPageCollectionRef1.setCollectionId(2L);
        visitBrandPageCollectionRef1.setStatus(true);
        visitBrandPageCollectionRef1.setTriggerId(4L);
        visitBrandPageCollectionRef1.setVisitBrandPageSetting(visitBrandPageSetting);
        visitBrandPageCollectionRefs.add(visitBrandPageCollectionRef1);
        VisitBrandPageCollectionRef visitBrandPageCollectionRef2 = new VisitBrandPageCollectionRef();
        visitBrandPageCollectionRef2.setCollectionId(3L);
        visitBrandPageCollectionRef2.setStatus(true);
        visitBrandPageCollectionRef2.setTriggerId(4L);
        visitBrandPageCollectionRef2.setVisitBrandPageSetting(visitBrandPageSetting);
        visitBrandPageCollectionRefs.add(visitBrandPageCollectionRef2);
        visitBrandPageSetting.setVisitBrandPageCollectionRefs(visitBrandPageCollectionRefs);
        List<VisitBrandPageURLFilterRef> visitBrandPageURLFilterRefs = new ArrayList<>();
        VisitBrandPageURLFilterRef visitBrandPageURLFilterRef1 = new VisitBrandPageURLFilterRef();
        visitBrandPageURLFilterRef1.setStatus(true);
        visitBrandPageURLFilterRef1.setTriggerId(4L);
        visitBrandPageURLFilterRef1.setUrlCriteria("Test Url Criteria 1");
        visitBrandPageURLFilterRef1.setVisitBrandPageSetting(visitBrandPageSetting);
        visitBrandPageURLFilterRefs.add(visitBrandPageURLFilterRef1);
        visitBrandPageSetting.setVisitBrandPageURLFilterRefs(visitBrandPageURLFilterRefs);
        service.saveVisitBrandPageSetting(visitBrandPageSetting);
        assertThat(visitBrandPageSetting.getId(), is(notNullValue()));
        assertThat(visitBrandPageSetting.getVisitBrandPageCollectionRefs().get(0).getId(), is(notNullValue()));
        assertThat(visitBrandPageSetting.getVisitBrandPageURLFilterRefs().get(0).getId(), is(notNullValue()));
    }
}
