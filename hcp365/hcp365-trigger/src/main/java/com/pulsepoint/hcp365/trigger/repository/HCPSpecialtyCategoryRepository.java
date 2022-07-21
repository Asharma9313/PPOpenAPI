package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.HCPSpecialtyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HCPSpecialtyCategoryRepository extends JpaRepository<HCPSpecialtyCategory, Long> {
    List<HCPSpecialtyCategory> findByIdIn(List<Long> ids);
    List<HCPSpecialtyCategory> findByGroupIdIn(List<Long> groupIds);
}
