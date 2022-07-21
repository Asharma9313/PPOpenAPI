package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.modal.ReportFormatSetting;
import com.pulsepoint.hcp365.repository.ReportFormatSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportFormatSettingService {
    private ReportFormatSetting reportFormatSetting;

    @Autowired
    ReportFormatSettingRepository repository;

    public ReportFormatSetting findByAccountIdAndAdvId(Long accountId, Long advId){
        return repository.findByAccountIdAndAdvertiserId(accountId, advId);
    }

    public ReportFormatSetting save(ReportFormatSetting reportFormatSetting){
        return repository.save(reportFormatSetting);
    }

}
