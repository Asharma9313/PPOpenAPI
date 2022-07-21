package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.dto.NPISmartListDTO;
import com.pulsepoint.hcp365.trigger.modal.NPISmartList;
import com.pulsepoint.hcp365.trigger.service.NPISmartListService;
import com.pulsepoint.hcp365.trigger.service.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/response/npismartlist")
@RefreshScope
public class NPISmartListController {

    @Autowired
    NPISmartListService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    public NPISmartListDTO getByTriggerId(@PathVariable("triggerId") Long triggerId){
        NPISmartList npiSmartList = service.findByTriggerId(triggerId);
        if(npiSmartList != null && npiSmartList.getStatus() == true) {
            return entityConverter.convertToDto(NPISmartListDTO.class, npiSmartList);
        }
        return null;
    }
    public List<NPISmartListDTO> getByTriggerIds(List<Long> triggerIds){
        List<NPISmartList> npiSmartLists = service.findByTriggerIds(triggerIds);
        List<NPISmartListDTO> npiSmartListDTOS = new ArrayList<>();
        if(CollectionUtils.isEmpty(npiSmartLists) == false) {
            npiSmartLists.stream().forEach(npiSmartList -> {
                if(npiSmartList.getStatus() == true) {
                    npiSmartListDTOS.add(entityConverter.convertToDto(NPISmartListDTO.class, npiSmartList));
                }
            });
        }
        return npiSmartListDTOS;
    }
    @PutMapping("")
    public NPISmartListDTO save(@PathVariable("triggerId") Long triggerId, @RequestBody NPISmartListDTO npiSmartListDTO){
        NPISmartList uiNpiSmartList = entityConverter.convertFromDto(NPISmartList.class, npiSmartListDTO);
        NPISmartList dbNpiSmartList = service.findByTriggerId(triggerId);
        if(dbNpiSmartList == null || dbNpiSmartList.getStatus() == false){
            uiNpiSmartList.setStatus(true);
            uiNpiSmartList.setTriggerId(triggerId);
            service.save(uiNpiSmartList, SecurityContextUtil.getAccountId());
        } else {
            entityConverter.mergeNPISmartList(uiNpiSmartList, dbNpiSmartList, triggerId);
            service.save(dbNpiSmartList, SecurityContextUtil.getAccountId());
        }
        return npiSmartListDTO;
    }

    @DeleteMapping("")
    public void delete(@PathVariable("triggerId") Long triggerId){
        NPISmartList dbNpiSmartList = service.findByTriggerId(triggerId);
        if(dbNpiSmartList != null) {
            dbNpiSmartList.setStatus(false);
            service.save(dbNpiSmartList, SecurityContextUtil.getAccountId());
        }
    }

}
