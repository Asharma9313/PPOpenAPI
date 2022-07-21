package com.pulsepoint.journey.audience;

import com.pulsepoint.journey.audience.enums.AudienceConditionType;
import com.pulsepoint.journey.audience.enums.Operator;
import com.pulsepoint.journey.audience.modal.*;
import com.pulsepoint.journey.audience.service.LifeAudienceDefinitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class LifeAudienceDefinitionServiceTest {
    @Autowired
    LifeAudienceDefinitionService lifeAudienceDefinitionService;

    @Test
    @Transactional
    public void testGetActiveAudience() {
        List<LifeAudienceDefinition> audienceDefinitions = lifeAudienceDefinitionService.findByAccountId(559145L);
        assertThat(audienceDefinitions.size(), is(equalTo(12)));
        assertThat(audienceDefinitions.get(0).getAudienceConditions().size(),is(equalTo(2)));
        assertThat(audienceDefinitions.get(0).getAudienceRecordingPixelXRefs().size(), is(equalTo(2)));
    }

    @Test
    public void testPagination(){
        Page<LifeAudienceDefinition> audienceDefinitions = lifeAudienceDefinitionService.search(559145L, "%Test Audience1%", 0);
        assertThat(audienceDefinitions.getTotalPages(), is(equalTo(1)));
        assertThat(audienceDefinitions.get().count(), is(equalTo(4L)));
    }

    @Test
    @Transactional
    public void testSaveLifeAudience(){
        LifeAudienceDefinition audienceDefinition = new LifeAudienceDefinition();
        audienceDefinition.setName("Test Audience111");
        audienceDefinition.setRecency(3);
        audienceDefinition.setFrequency(10);
        audienceDefinition.setUrlMask("Test URL");
        audienceDefinition.setAccountId(559145L);
        audienceDefinition.setPixelId(22L);
        audienceDefinition.setSegmentName("test seg,emt");
        AudienceCondition audienceCondition = new AudienceCondition();
        audienceCondition.setActive(true);
        audienceCondition.setAudienceDefinition(audienceDefinition);
        audienceCondition.setConditionType(AudienceConditionType.QUERY_PARAM);
        audienceCondition.setConditionValue("Test");
        audienceCondition.setOperator(Operator.EQUALS);
        audienceDefinition.setAudienceConditions(new ArrayList<>());
        audienceDefinition.getAudienceConditions().add(audienceCondition);
        lifeAudienceDefinitionService.saveOrUpdate(audienceDefinition);
        assertThat(audienceDefinition.getId(), is(greaterThan(0L)));
        assertThat(audienceDefinition.getAudienceConditions().get(0).getId(), is(greaterThan(0L)));
        audienceDefinition = (LifeAudienceDefinition) lifeAudienceDefinitionService.getById(audienceDefinition.getId());
        AudienceRecordingPixelXRef recordingPixelXRef = new AudienceRecordingPixelXRef();
        recordingPixelXRef.setAudienceDefinition(audienceDefinition);
        recordingPixelXRef.setPixelId(22L);
        recordingPixelXRef.setActive(true);
        audienceDefinition.setAudienceRecordingPixelXRefs(new ArrayList<>());
        audienceDefinition.getAudienceRecordingPixelXRefs().add(recordingPixelXRef);
        lifeAudienceDefinitionService.saveOrUpdate(audienceDefinition);
        assertThat(audienceDefinition.getAudienceRecordingPixelXRefs().get(0).getId(), is(greaterThan(0L)));
    }
    //@Test
    @Transactional
    public void testSavePublisherAudience(){
        PublisherAudienceDefinition audienceDefinition = new PublisherAudienceDefinition();
        audienceDefinition.setName("Test Audience111");
        audienceDefinition.setRecency(3);
        audienceDefinition.setFrequency(10);
        audienceDefinition.setUrlMask("Test URL");
        audienceDefinition.setAccountId(559145L);
        audienceDefinition.setDealName("Test Deal");
        audienceDefinition.setDealPrice(4.5);
        AudienceCondition audienceCondition = new AudienceCondition();
        audienceCondition.setActive(true);
        AudienceDealMappingXRef dealMappingXRef = new AudienceDealMappingXRef();
        dealMappingXRef.setAudienceDefinition(audienceDefinition);
        dealMappingXRef.setDspId(559645L);
        dealMappingXRef.setInternalDealId(92L);
        dealMappingXRef.setActive(true);
        List<AudienceDealMappingXRef> dealMappingXRefList = new ArrayList<>();
        dealMappingXRefList.add(dealMappingXRef);
        audienceDefinition.setAudienceDealMappingXRefs(dealMappingXRefList);
        audienceCondition.setAudienceDefinition(audienceDefinition);
        audienceCondition.setConditionType(AudienceConditionType.QUERY_PARAM);
        audienceCondition.setConditionValue("Test");
        audienceCondition.setOperator(Operator.EQUALS);
        audienceDefinition.setAudienceConditions(new ArrayList<>());
        audienceDefinition.getAudienceConditions().add(audienceCondition);
        //audienceDefinitionService.saveOrUpdate(audienceDefinition);
        assertThat(audienceDefinition.getId(), is(greaterThan(0L)));
        assertThat(audienceDefinition.getAudienceConditions().get(0).getId(), is(greaterThan(0L)));
        assertThat(audienceDefinition.getAudienceDealMappingXRefs().get(0).getId(), is(greaterThan(0L)));
        AudienceRecordingPixelXRef recordingPixelXRef = new AudienceRecordingPixelXRef();
        recordingPixelXRef.setAudienceDefinition(audienceDefinition);
        recordingPixelXRef.setPixelId(22L);
        recordingPixelXRef.setActive(true);
        audienceDefinition.setAudienceRecordingPixelXRefs(new ArrayList<>());
        audienceDefinition.getAudienceRecordingPixelXRefs().add(recordingPixelXRef);
        //audienceDefinitionService.saveOrUpdate(audienceDefinition);
        assertThat(audienceDefinition.getAudienceRecordingPixelXRefs().get(0).getId(), is(greaterThan(0L)));
    }
}
