package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.ClickEmailSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.service.ClickEmailSettingCollectionRefService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ClickEmailSettingCollectionRefServiceTest {
    @Autowired
    ClickEmailSettingCollectionRefService service;

    @Test
    public void testGetByTriggerId(){
        List<ClickEmailSettingCollectionRef> refs= service.findByTriggerId(1L);
        assertThat(refs.size(), is(equalTo(2)));
    }

    @Test
    public void testCreateNew(){
        ClickEmailSettingCollectionRef ref = new ClickEmailSettingCollectionRef();
        ref.setCollectionId(1L);
        ref.setStatus(true);
        ref.setTriggerId(4L);
    }
}
