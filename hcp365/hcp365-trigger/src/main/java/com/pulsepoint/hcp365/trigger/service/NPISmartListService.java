package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.hcp365.trigger.modal.NPISmartList;
import com.pulsepoint.hcp365.trigger.repository.NPISmartListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NPISmartListService {

    @Autowired
    NPISmartListRepository repository;

    public NPISmartList findByTriggerId(Long triggerId){
        return repository.findByTriggerIdAndStatus(triggerId, true);
    }

    public List<NPISmartList> findByTriggerIds(List<Long> triggerIds){
        return repository.findByTriggerIdIn(triggerIds);
    }
    public List<NPISmartList> findByGroupId(Long groupId){
        return repository.findByGroupId(groupId);
    }
    public NPISmartList save(NPISmartList  npiSmartList, Long accountId){
        //validateNPIListName(npiSmartList, accountId);
        return repository.save(npiSmartList);
    }
    /*private void validateNPIListName(NPISmartList  npiSmartList, Long accountId){
        List<NPISmartList> listWithSameName = repository.findBySmartListNameAndTriggerAccountIdAndStatus(npiSmartList.getSmartListName(), accountId, true);
        if(listWithSameName.size() > 0){
            if(npiSmartList.getId() == null || (npiSmartList.getId() > 0 && (listWithSameName.size() != 1 || listWithSameName.get(0).getId().equals(npiSmartList.getId()) == false))){
                throw new InvalidDataException("NPI Smart List " +  npiSmartList.getSmartListName() + " already exists.");
            }
        }
    }*/
}
