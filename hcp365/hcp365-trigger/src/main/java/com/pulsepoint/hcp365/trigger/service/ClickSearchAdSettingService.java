package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSetting;
import com.pulsepoint.hcp365.trigger.repository.ClickSearchAdSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ClickSearchAdSettingService {
    @Autowired
    ClickSearchAdSettingRepository repository;

    public ClickSearchAdSetting findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public ClickSearchAdSetting saveClickSearchAdSetting(ClickSearchAdSetting clickSearchAdSetting){
        return repository.save(clickSearchAdSetting);
    }

    public List<ClickSearchAdSetting> getByTriggerIds(List<Long> triggerIds){
        return repository.findByTriggerIdIn(triggerIds);
    }

    public List<ClickSearchAdSetting> findByCollectionId(Long collectionId){
        List<ClickSearchAdSetting> clickSearchAdSettings = repository.findByClickSearchAdSettingCollectionRefsCollectionId(collectionId);
        if(CollectionUtils.isEmpty(clickSearchAdSettings)){
            return null;
        }
        return clickSearchAdSettings;
    }
}
