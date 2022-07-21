package com.pulsepoint.hcp365.trigger;

import com.pulsepoint.hcp365.trigger.modal.Webhook;
import com.pulsepoint.hcp365.trigger.service.WebhookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = "spring.cloud.config.enabled=false")
public class WebhookServiceTest {
    @Autowired
    WebhookService service;

    @Test
    public void testGetByTriggerId(){
        Webhook webhook = service.findByTriggerId(1L);
        assertThat(webhook.getId(), is(equalTo(1L)));
    }

    @Test
    public void testCreateNew(){
        Webhook webhook = new Webhook();
        webhook.setUrl("http://samplewebhook/workingsample");
        //webhook.setNpiMacro("%NPIMacro%");
        webhook.setTriggerId(4L);
        service.save(webhook);
        assertThat(webhook.getId(), is(notNullValue()));
    }
}
