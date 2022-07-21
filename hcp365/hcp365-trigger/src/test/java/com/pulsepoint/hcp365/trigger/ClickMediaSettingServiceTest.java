package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.ClickMediaSetting;
import com.pulsepoint.hcp365.trigger.modal.ClickMediaSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.repository.ClickMediaSettingRepository;
import com.pulsepoint.hcp365.trigger.service.ClickMediaSettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ClickMediaSettingServiceTest {
    @Autowired
    ClickMediaSettingRepository repository;

    @Autowired
    ClickMediaSettingService service;

    @Test
    public void testGetByTriggerId(){
        ClickMediaSetting ClickMediaSetting = service.findByTriggerId(1L);
        assertThat(ClickMediaSetting, is(notNullValue()));
    }

    @Test
    public void testCreate(){
        ClickMediaSetting ClickMediaSetting = new ClickMediaSetting();
        ClickMediaSetting.setTriggerId(4L);
        ClickMediaSetting.setFrequencyControlValue(3);
        ClickMediaSetting.setClickMediaSettingCollectionRefs(new ArrayList<>());
        ClickMediaSettingCollectionRef ClickMediaSettingCollectionRef1 = new ClickMediaSettingCollectionRef();
        ClickMediaSettingCollectionRef1.setCollectionId(2L);
        ClickMediaSettingCollectionRef1.setClickMediaSetting(ClickMediaSetting);
        ClickMediaSettingCollectionRef1.setStatus(true);
        ClickMediaSettingCollectionRef1.setTriggerId(4L);
        ClickMediaSetting.getClickMediaSettingCollectionRefs().add(ClickMediaSettingCollectionRef1);
        ClickMediaSettingCollectionRef ClickMediaSettingCollectionRef2 = new ClickMediaSettingCollectionRef();
        ClickMediaSettingCollectionRef2.setCollectionId(3L);
        ClickMediaSettingCollectionRef2.setClickMediaSetting(ClickMediaSetting);
        ClickMediaSettingCollectionRef2.setStatus(true);
        ClickMediaSettingCollectionRef2.setTriggerId(4L);
        ClickMediaSetting.getClickMediaSettingCollectionRefs().add(ClickMediaSettingCollectionRef2);
        service.save(ClickMediaSetting);
    }
}
