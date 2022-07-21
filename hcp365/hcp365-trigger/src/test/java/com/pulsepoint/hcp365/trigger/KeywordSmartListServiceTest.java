package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.KeywordSmartList;
import com.pulsepoint.hcp365.trigger.service.KeywordSmartListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class KeywordSmartListServiceTest {
    @Autowired
    KeywordSmartListService service;

    @Test
    public void testGetByTriggerId(){
        KeywordSmartList KeywordSmartList = service.findByTriggerId(1L);
        assertThat(KeywordSmartList.getTriggerId(), is(equalTo(1L)));
    }

    @Test
    public void createNew(){
        KeywordSmartList keywordSmartList = new KeywordSmartList();
        keywordSmartList.setTriggerId(4L);
        keywordSmartList.setRemoveAfter(10);
        //KeywordSmartList.setSmartListName("Test List2");
        keywordSmartList.setGroupId(44L);
        service.save(keywordSmartList, 559145L);
        assertThat(keywordSmartList.getId(), is(notNullValue()));
    }
}
