package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.OpenEmailSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.repository.OpenEmailSettingCollectionRefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenEmailSettingCollectionRefService {

    @Autowired
    OpenEmailSettingCollectionRefRepository repository;

    public List<OpenEmailSettingCollectionRef> findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public List<OpenEmailSettingCollectionRef> save(List<OpenEmailSettingCollectionRef> ref){
        return repository.saveAll(ref);
    }
}
