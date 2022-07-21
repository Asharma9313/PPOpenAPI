package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageSetting;
import com.pulsepoint.hcp365.trigger.repository.VisitBrandPageSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitBrandPageSettingService {
    @Autowired
    VisitBrandPageSettingRepository repository;

    public VisitBrandPageSetting findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }

    public VisitBrandPageSetting saveVisitBrandPageSetting(VisitBrandPageSetting visitBrandPageSetting){
        return repository.save(visitBrandPageSetting);
    }

    public List<VisitBrandPageSetting> findByCollectionId(Long collectionId){
        List<VisitBrandPageSetting> visitBrandPageSettingList = repository.findByVisitBrandPageCollectionRefsCollectionId(collectionId);
        if(CollectionUtils.isEmpty(visitBrandPageSettingList)){
            return null;
        }
        return visitBrandPageSettingList;
    }

    public List<VisitBrandPageSetting> findByTriggerIds(List<Long> triggerIds){
        return repository.findByTriggerIdIn(triggerIds);
    }
}
