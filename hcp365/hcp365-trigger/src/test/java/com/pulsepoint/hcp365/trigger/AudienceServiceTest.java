package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.enums.AudienceType;
import com.pulsepoint.hcp365.trigger.enums.Operator;
import com.pulsepoint.hcp365.trigger.modal.Audience;
import com.pulsepoint.hcp365.trigger.service.AudienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class AudienceServiceTest {
    @Autowired
    AudienceService audienceService;

    @Test
    public void testGetActiveAudience() {
        List<Audience> audienceList = audienceService.findActiveAudience(1L, AudienceType.NPILIST);
        assertThat(audienceList.size(), is(equalTo(2)));
    }

    @Test
    public void testGetAllAudience() {
        List<Audience> audienceList = audienceService.findAllAudience(1L, AudienceType.NPILIST);
        assertThat(audienceList.size(), is(equalTo(3)));
    }

    @Test
    public void testCreateAudience() {
        List<Audience> audienceList = new ArrayList<>();
        audienceList.add(new Audience(null, 2L, AudienceType.GEOTARGET, 10L, Operator.BLOCK, true));
        audienceList.add(new Audience(null, 2L, AudienceType.GEOTARGET, 30L, Operator.TARGET, true));
        audienceList = audienceService.save(audienceList);
        assertThat(audienceList.get(0).getId(), is(notNullValue()));
    }
}
