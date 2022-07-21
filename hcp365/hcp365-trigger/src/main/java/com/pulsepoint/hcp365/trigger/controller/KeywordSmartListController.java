package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.dto.KeywordSmartListDTO;
import com.pulsepoint.hcp365.trigger.modal.KeywordSmartList;
import com.pulsepoint.hcp365.trigger.service.KeywordSmartListService;
import com.pulsepoint.hcp365.trigger.service.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/response/keywordsmartlist")
@RefreshScope
public class KeywordSmartListController {

    @Autowired
    KeywordSmartListService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    public KeywordSmartListDTO getByTriggerId(@PathVariable("triggerId") Long triggerId){
        KeywordSmartList KeywordSmartList = service.findByTriggerId(triggerId);
        if(KeywordSmartList != null && KeywordSmartList.getStatus() == true) {
            return entityConverter.convertToDto(KeywordSmartListDTO.class, KeywordSmartList);
        }
        return null;
    }

    @PutMapping("")
    public KeywordSmartListDTO save(@PathVariable("triggerId") Long triggerId, @RequestBody KeywordSmartListDTO KeywordSmartListDTO){
        KeywordSmartList uiKeywordSmartList = entityConverter.convertFromDto(KeywordSmartList.class, KeywordSmartListDTO);
        KeywordSmartList dbKeywordSmartList = service.findByTriggerId(triggerId);
        if(dbKeywordSmartList == null){
            uiKeywordSmartList.setStatus(true);
            uiKeywordSmartList.setTriggerId(triggerId);
            service.save(uiKeywordSmartList, SecurityContextUtil.getAccountId());
        } else {
            entityConverter.mergeKeywordSmartList(uiKeywordSmartList, dbKeywordSmartList, triggerId);
            service.save(dbKeywordSmartList, SecurityContextUtil.getAccountId());
        }
        return KeywordSmartListDTO;
    }

    @DeleteMapping("")
    public void delete(@PathVariable("triggerId") Long triggerId){
        KeywordSmartList dbKeywordSmartList = service.findByTriggerId(triggerId);
        if(dbKeywordSmartList != null) {
            dbKeywordSmartList.setStatus(false);
            service.save(dbKeywordSmartList, SecurityContextUtil.getAccountId());
        }
    }

}
