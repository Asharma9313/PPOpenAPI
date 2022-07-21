package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.Webhook;
import com.pulsepoint.hcp365.trigger.modal.WebhookTriggerContentType;
import com.pulsepoint.hcp365.trigger.modal.WebhookTriggerRequestMethod;
import com.pulsepoint.hcp365.trigger.repository.WebhookRepository;
import com.pulsepoint.hcp365.trigger.repository.WebhookTriggerContentTypeRepository;
import com.pulsepoint.hcp365.trigger.repository.WebhookTriggerRequestMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebhookService {

    @Autowired
    WebhookRepository repository;

    @Autowired
    WebhookTriggerContentTypeRepository webhookTriggerContentTypeRepository;

    @Autowired
    WebhookTriggerRequestMethodRepository webhookTriggerRequestMethodRepository;

    public Webhook findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public Webhook save(Webhook webhook){
        return repository.save(webhook);
    }

    public List<WebhookTriggerContentType> getWebhookTriggerContentTypes() {
        return webhookTriggerContentTypeRepository.findAll();
    }

    public List<WebhookTriggerRequestMethod> getWebhookTriggerRequestMethods() {
        return webhookTriggerRequestMethodRepository.findAll();
    }
}
