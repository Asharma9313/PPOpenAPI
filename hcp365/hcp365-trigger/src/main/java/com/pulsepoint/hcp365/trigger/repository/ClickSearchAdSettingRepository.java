package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickSearchAdSettingRepository extends JpaRepository<ClickSearchAdSetting, Long>, JpaSpecificationExecutor<ClickSearchAdSetting> {
    ClickSearchAdSetting findByTriggerId(Long triggerId);
    List<ClickSearchAdSetting> findByTriggerIdIn(List<Long> triggerIds);
    List<ClickSearchAdSetting> findByClickSearchAdSettingCollectionRefsCollectionId(Long collectionId);
}
