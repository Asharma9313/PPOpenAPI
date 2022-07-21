package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitBrandPageSettingRepository extends JpaRepository<VisitBrandPageSetting, Long>, JpaSpecificationExecutor<VisitBrandPageSetting> {
    VisitBrandPageSetting findByTriggerId(Long triggerId);
    List<VisitBrandPageSetting> findByVisitBrandPageCollectionRefsCollectionId(Long collectionId);
    List<VisitBrandPageSetting> findByTriggerIdIn(List<Long> triggerIds);
}
