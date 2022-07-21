package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.ClickMediaSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClickMediaSettingRepository extends JpaRepository<ClickMediaSetting, Long>, JpaSpecificationExecutor<ClickMediaSetting> {
    ClickMediaSetting findByTriggerId(Long triggerId);
}
