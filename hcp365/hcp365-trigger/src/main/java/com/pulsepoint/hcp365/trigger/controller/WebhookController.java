package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.dto.WebhookDTO;
import com.pulsepoint.hcp365.trigger.modal.Webhook;
import com.pulsepoint.hcp365.trigger.service.TriggerService;
import com.pulsepoint.hcp365.trigger.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/response/webhook")
@RefreshScope
public class WebhookController {

    @Autowired
    WebhookService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    public WebhookDTO getByTriggerId(@PathVariable("triggerId") Long triggerId){
        Webhook webhook = service.findByTriggerId(triggerId);
        if(webhook != null && webhook.getStatus() == true) {
            return entityConverter.convertToDto(WebhookDTO.class, webhook);
        }
        return null;
    }

    @PutMapping("")
    public WebhookDTO save(@PathVariable("triggerId") Long triggerId, @RequestBody WebhookDTO webhookDTO){
        Webhook dbWebhook = service.findByTriggerId(triggerId);
        Webhook uiWebhook = entityConverter.convertFromDto(Webhook.class, webhookDTO);
        if(dbWebhook == null){
            uiWebhook.setStatus(true);
            uiWebhook.setTriggerId(triggerId);
            service.save(uiWebhook);
        } else{
            entityConverter.mergeWebhook(uiWebhook, dbWebhook);
            service.save(dbWebhook);
        }
        return webhookDTO;
    }

    @DeleteMapping("")
    public void delete(@PathVariable("triggerId") Long triggerId){
        Webhook dbWebhook = service.findByTriggerId(triggerId);
        if(dbWebhook != null) {
            dbWebhook.setStatus(false);
            service.save(dbWebhook);
        }
    }
}
