package com.pulsepoint.journey.audience.repository;

import com.pulsepoint.journey.audience.modal.DSP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DSPRepository extends JpaRepository<DSP, Long> {

}
