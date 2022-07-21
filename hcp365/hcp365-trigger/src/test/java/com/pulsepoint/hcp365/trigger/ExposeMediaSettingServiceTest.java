package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.ExposeMediaSetting;
import com.pulsepoint.hcp365.trigger.modal.ExposeMediaSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.service.ExposeMediaSettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ExposeMediaSettingServiceTest {
    @Autowired
    ExposeMediaSettingService service;

    @Test
    public void testGetByTriggerId(){
        ExposeMediaSetting exposeMediaSetting = service.findByTriggerId(1L);
        assertThat(exposeMediaSetting, is(notNullValue()));
    }

    @Test
    public void testCreate(){
        ExposeMediaSetting exposeMediaSetting = new ExposeMediaSetting();
        exposeMediaSetting.setTriggerId(4L);
        exposeMediaSetting.setFrequencyControlValue(3);
        exposeMediaSetting.setExposeMediaSettingCollectionRefs(new ArrayList<>());
        ExposeMediaSettingCollectionRef exposeMediaSettingCollectionRef1 = new ExposeMediaSettingCollectionRef();
        exposeMediaSettingCollectionRef1.setCollectionId(2L);
        exposeMediaSettingCollectionRef1.setExposeMediaSetting(exposeMediaSetting);
        exposeMediaSettingCollectionRef1.setStatus(true);
        exposeMediaSettingCollectionRef1.setTriggerId(4L);
        exposeMediaSetting.getExposeMediaSettingCollectionRefs().add(exposeMediaSettingCollectionRef1);
        ExposeMediaSettingCollectionRef exposeMediaSettingCollectionRef2 = new ExposeMediaSettingCollectionRef();
        exposeMediaSettingCollectionRef2.setCollectionId(3L);
        exposeMediaSettingCollectionRef2.setExposeMediaSetting(exposeMediaSetting);
        exposeMediaSettingCollectionRef2.setStatus(true);
        exposeMediaSettingCollectionRef2.setTriggerId(4L);
        exposeMediaSetting.getExposeMediaSettingCollectionRefs().add(exposeMediaSettingCollectionRef2);
        service.save(exposeMediaSetting);
    }
}
