package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.WebhookTriggerRequestMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookTriggerRequestMethodRepository extends JpaRepository<WebhookTriggerRequestMethod, Long> {
}
