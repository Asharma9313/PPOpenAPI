package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookRepository extends JpaRepository<Webhook, Long> {
    Webhook findByTriggerId(Long triggerId);
}
