package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.ExposeMediaSetting;
import com.pulsepoint.hcp365.trigger.repository.ExposeMediaSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExposeMediaSettingService {

    @Autowired
    ExposeMediaSettingRepository repository;

    public ExposeMediaSetting findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public ExposeMediaSetting save(ExposeMediaSetting exposeMediaSetting){
        return repository.save(exposeMediaSetting);
    }
}
