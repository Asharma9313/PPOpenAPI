package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSetting;
import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSettingKeywordRef;
import com.pulsepoint.hcp365.trigger.service.ClickSearchAdSettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class ClickSearchAdSettingServiceTest {
    @Autowired
    ClickSearchAdSettingService service;

    @Test
    public void testGetClickSearchAdSetting(){
        ClickSearchAdSetting clickSearchAdSetting = service.findByTriggerId(1L);
        assertThat(clickSearchAdSetting, is(notNullValue()));
        assertThat(clickSearchAdSetting.getTriggerId(), is(equalTo(1L)));
    }

    @Test
    public void testCrateNew(){
        ClickSearchAdSetting clickSearchAdSetting = new ClickSearchAdSetting();
        clickSearchAdSetting.setTriggerId(4L);
        clickSearchAdSetting.setCustomUrlParamName("Param 2");
        clickSearchAdSetting.setCustomUrlParamValue("Test Param Value");
        clickSearchAdSetting.setCustomKeywordQueryOperator(Operator.TARGET);
        clickSearchAdSetting.setClickSearchAdSettingCollectionRefs(new ArrayList<>());
        clickSearchAdSetting.setClickSearchAdSettingKeywordRefs(new ArrayList<>());
        ClickSearchAdSettingCollectionRef clickSearchAdSettingCollectionRef1 = new ClickSearchAdSettingCollectionRef();
        clickSearchAdSettingCollectionRef1.setClickSearchAdSetting(clickSearchAdSetting);
        clickSearchAdSettingCollectionRef1.setCollectionId(1L);
        clickSearchAdSettingCollectionRef1.setStatus(true);
        clickSearchAdSettingCollectionRef1.setTriggerId(4L);
        clickSearchAdSetting.getClickSearchAdSettingCollectionRefs().add(clickSearchAdSettingCollectionRef1);
        ClickSearchAdSettingCollectionRef clickSearchAdSettingCollectionRef2 = new ClickSearchAdSettingCollectionRef();
        clickSearchAdSettingCollectionRef2.setClickSearchAdSetting(clickSearchAdSetting);
        clickSearchAdSettingCollectionRef2.setCollectionId(2L);
        clickSearchAdSettingCollectionRef2.setStatus(true);
        clickSearchAdSettingCollectionRef2.setTriggerId(4L);
        clickSearchAdSetting.getClickSearchAdSettingCollectionRefs().add(clickSearchAdSettingCollectionRef2);

        ClickSearchAdSettingKeywordRef clickSearchAdSettingKeywordRef = new ClickSearchAdSettingKeywordRef();
        clickSearchAdSettingKeywordRef.setClickSearchAdSetting(clickSearchAdSetting);
        clickSearchAdSettingKeywordRef.setKeyword("Keyword1");
        clickSearchAdSettingKeywordRef.setTriggerId(4L);
        clickSearchAdSettingKeywordRef.setStatus(true);
        clickSearchAdSetting.getClickSearchAdSettingKeywordRefs().add(clickSearchAdSettingKeywordRef);
        service.saveClickSearchAdSetting(clickSearchAdSetting);
        assertThat(clickSearchAdSetting.getId(), is(notNullValue()));
        assertThat(clickSearchAdSetting.getClickSearchAdSettingCollectionRefs().get(0).getId(), is(notNullValue()));
        assertThat(clickSearchAdSetting.getClickSearchAdSettingKeywordRefs().get(0).getId(), is(notNullValue()));
    }
}
