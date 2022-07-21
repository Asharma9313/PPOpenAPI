package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.WebhookTriggerContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookTriggerContentTypeRepository extends JpaRepository<WebhookTriggerContentType, Long> {
}
