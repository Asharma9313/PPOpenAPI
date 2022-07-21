package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.NPISmartList;
import com.pulsepoint.hcp365.trigger.service.NPISmartListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class NPISmartListServiceTest {
    @Autowired
    NPISmartListService service;

    @Test
    public void testGetByTriggerId(){
        NPISmartList npiSmartList = service.findByTriggerId(1L);
        assertThat(npiSmartList.getTriggerId(), is(equalTo(1L)));
    }

    @Test
    public void createNew(){
        NPISmartList npiSmartList = new NPISmartList();
        npiSmartList.setTriggerId(4L);
        npiSmartList.setRemoveAfter(10);
        //npiSmartList.setSmartListName("Test List2");
        npiSmartList.setGroupId(445L);
        service.save(npiSmartList, 559145L);
        assertThat(npiSmartList.getId(), is(notNullValue()));
    }
}
