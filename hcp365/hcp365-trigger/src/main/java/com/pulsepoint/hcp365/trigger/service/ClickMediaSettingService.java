package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.ClickMediaSetting;
import com.pulsepoint.hcp365.trigger.repository.ClickMediaSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClickMediaSettingService {
    @Autowired
    ClickMediaSettingRepository repository;

    public ClickMediaSetting findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public ClickMediaSetting save(ClickMediaSetting clickMediaSetting){
        return repository.save(clickMediaSetting);
    }
}
