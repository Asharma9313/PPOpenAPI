package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.ClickEmailSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.repository.ClickEmailSettingCollectionRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClickEmailSettingCollectionRefService {

    @Autowired
    ClickEmailSettingCollectionRefRepository repository;

    public List<ClickEmailSettingCollectionRef> findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public List<ClickEmailSettingCollectionRef> save(List<ClickEmailSettingCollectionRef> ref){
        return repository.saveAll(ref);
    }
}
