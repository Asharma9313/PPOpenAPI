package com.pulsepoint.journey.audience.repository;

import com.pulsepoint.journey.audience.modal.DSPSpend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DSPSpendRepository extends JpaRepository<DSPSpend, Long> {
    List<DSPSpend> findTop5ByAudienceIdOrderBySpendDesc(Long audienceId);
}
