package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.ReportFormatSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportFormatSettingRepository extends JpaRepository<ReportFormatSetting, Long> {
    ReportFormatSetting findByAccountIdAndAdvertiserId(Long accountId, Long advertiserId);
}
