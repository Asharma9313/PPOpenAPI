package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.TimeZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends JpaRepository<TimeZone, Long> {
}
