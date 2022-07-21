package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.dto.VisitBrandPageSettingDTO;
import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageSetting;
import com.pulsepoint.hcp365.trigger.service.VisitBrandPageSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/visitbrandpage")
@RefreshScope
public class VisitBrandPageSettingController {

    @Autowired
    VisitBrandPageSettingService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    public VisitBrandPageSettingDTO getByTriggerId(@PathVariable("triggerId") Long triggerId) {
        VisitBrandPageSetting visitBrandPageSetting = service.findByTriggerId(triggerId);
        if(visitBrandPageSetting == null || visitBrandPageSetting.getStatus() == false){
            return null;
        }
        return entityConverter.convertToVisitBrandPageSettingDTO(visitBrandPageSetting);
    }

    @PostMapping("")
    public VisitBrandPageSettingDTO createNew(@PathVariable("triggerId") Long triggerId, @RequestBody VisitBrandPageSettingDTO visitBrandPageSettingDTO) {
        VisitBrandPageSetting dbVisitBrandPageSetting = service.findByTriggerId(triggerId);
        if(dbVisitBrandPageSetting == null) {
            VisitBrandPageSetting visitBrandPageSetting = entityConverter.convertFromVisitBrandPageSettingDTO(visitBrandPageSettingDTO, triggerId);
            service.saveVisitBrandPageSetting(visitBrandPageSetting);
        } else{
            update(triggerId, visitBrandPageSettingDTO);
        }
        return visitBrandPageSettingDTO;
    }

    @PutMapping("")
    public  VisitBrandPageSettingDTO update(@PathVariable("triggerId") Long triggerId, @RequestBody VisitBrandPageSettingDTO visitBrandPageSettingDTO){
        VisitBrandPageSetting dbVisitBrandPageSetting = service.findByTriggerId(triggerId);
        VisitBrandPageSetting uiVisitBrandPageSetting = entityConverter.convertFromVisitBrandPageSettingDTO(visitBrandPageSettingDTO, triggerId);
        if(dbVisitBrandPageSetting == null){
            service.saveVisitBrandPageSetting(uiVisitBrandPageSetting);
        } else {
            dbVisitBrandPageSetting = entityConverter.mergeVisitBrandPageSettingChanges(dbVisitBrandPageSetting, uiVisitBrandPageSetting);
            service.saveVisitBrandPageSetting(dbVisitBrandPageSetting);
        }
        return visitBrandPageSettingDTO;
    }

    @DeleteMapping()
    public void delete(@PathVariable("triggerId") Long triggerId){
        VisitBrandPageSetting visitBrandPageSetting = service.findByTriggerId(triggerId);
        if(visitBrandPageSetting != null) {
            visitBrandPageSetting.setStatus(false);
            service.saveVisitBrandPageSetting(visitBrandPageSetting);
        }
    }

    public List<VisitBrandPageSettingDTO> getByTriggerIds(List<Long> triggerIds){
        List<VisitBrandPageSetting> visitBrandPageSettings = service.findByTriggerIds(triggerIds);
        if(CollectionUtils.isEmpty(visitBrandPageSettings)){
            return  null;
        }
        visitBrandPageSettings = visitBrandPageSettings.stream().filter(v -> v.getStatus() == true).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(visitBrandPageSettings)){
            return  null;
        }
        List<VisitBrandPageSettingDTO> visitBrandPageSettingDTOS = new ArrayList<>();
        visitBrandPageSettings.stream().forEach(visitBrandPageSetting -> {
            visitBrandPageSettingDTOS.add(entityConverter.convertToVisitBrandPageSettingDTO(visitBrandPageSetting));
        });
        return visitBrandPageSettingDTOS;
    }
}
