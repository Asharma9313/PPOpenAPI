package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.Trigger;
import com.pulsepoint.hcp365.trigger.service.TriggerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class TriggerServiceTest {
    @Autowired
    TriggerService triggerService;

    @Test
    public void testGetByAdvId() {
        List<Trigger> triggerList = triggerService.findByAccountIdAndAdvId(559145L, 1L, false);
        assertThat(triggerList.size(), is(equalTo(2)));
    }

    @Test
    public void testCreateTrigger() {
        Trigger trigger = new Trigger();
        trigger.setName("Test Trigger 44");
        trigger.setAccountId(559145L);
        trigger.setAdvId(2L);
        trigger.setActive(true);
        trigger.setFromLife(false);
        trigger = triggerService.save(trigger);
        assertThat(trigger.getId(), is(greaterThan(0L)));
    }

    @Test
    public void testUpdateTrigger() {
        Trigger trigger = triggerService.findById(1L);
        trigger.setName("Modified Name");
        triggerService.save(trigger);
        trigger = triggerService.findById(1L);
        assertThat(trigger.getName(), is(equalTo("Modified Name")));
    }


}
