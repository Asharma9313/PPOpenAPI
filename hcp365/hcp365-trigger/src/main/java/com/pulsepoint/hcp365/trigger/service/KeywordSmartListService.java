package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.hcp365.trigger.modal.KeywordSmartList;
import com.pulsepoint.hcp365.trigger.repository.KeywordSmartListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordSmartListService {

    @Autowired
    KeywordSmartListRepository repository;

    public KeywordSmartList findByTriggerId(Long triggerId){
        return repository.findByTriggerId(triggerId);
    }
    public KeywordSmartList save(KeywordSmartList  KeywordSmartList, Long accountId){
        //validateKeywordListName(KeywordSmartList, accountId);
        return repository.save(KeywordSmartList);
    }
    /*private void validateKeywordListName(KeywordSmartList  KeywordSmartList, Long accountId){
        List<KeywordSmartList> listWithSameName = repository.findBySmartListNameAndTriggerAccountIdAndStatus(KeywordSmartList.getSmartListName(), accountId, true);
        if(listWithSameName.size() > 0){
            if(KeywordSmartList.getId() == null || (KeywordSmartList.getId() > 0 && (listWithSameName.size() != 1 || listWithSameName.get(0).getId().equals(KeywordSmartList.getId()) == false))){
                throw new InvalidDataException("Keyword Smart List " +  KeywordSmartList.getSmartListName() + " already exists.");
            }
        }
    }*/
}
