package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.ExposeMediaSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExposeMediaSettingRepository extends JpaRepository<ExposeMediaSetting, Long>, JpaSpecificationExecutor<ExposeMediaSetting> {
    ExposeMediaSetting findByTriggerId(Long triggerId);
}
