package com.pulsepoint.journey.audience.repository;

import com.pulsepoint.journey.audience.modal.Scorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScorecardRepository extends JpaRepository<Scorecard, Long> {
    public Scorecard findByAudienceId(Long audienceId);
}
