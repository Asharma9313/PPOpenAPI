package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.dto.ExposeMediaSettingDTO;
import com.pulsepoint.hcp365.trigger.modal.ExposeMediaSetting;
import com.pulsepoint.hcp365.trigger.service.ExposeMediaSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/exposemedia")
@RefreshScope
public class ExposeMediaSettingController {

    @Autowired
    ExposeMediaSettingService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    ExposeMediaSettingDTO geByTriggerId(@PathVariable("triggerId") Long triggerId){
        ExposeMediaSetting exposeMediaSetting = service.findByTriggerId(triggerId);
        if(exposeMediaSetting != null && exposeMediaSetting.getStatus() == true) {
            return entityConverter.convertToExposeMediaSettingDTO(exposeMediaSetting);
        } else{
            return null;
        }
    }

    @PostMapping("")
    ExposeMediaSettingDTO createNew(@PathVariable("triggerId") Long triggerId, @RequestBody ExposeMediaSettingDTO exposeMediaSettingDTO){
        ExposeMediaSetting exposeMediaSetting = entityConverter.convertFromExposeMediaSettingDTO(exposeMediaSettingDTO, triggerId);
        service.save(exposeMediaSetting);
        return exposeMediaSettingDTO;
    }

    @PutMapping("")
    ExposeMediaSettingDTO update(@PathVariable("triggerId") Long triggerId, @RequestBody ExposeMediaSettingDTO exposeMediaSettingDTO){
        ExposeMediaSetting uiExposeMediaSetting = entityConverter.convertFromExposeMediaSettingDTO(exposeMediaSettingDTO, triggerId);
        ExposeMediaSetting dbExposeMediaSetting = service.findByTriggerId(triggerId);
        if(dbExposeMediaSetting == null){
            service.save(uiExposeMediaSetting);
        } else {
            dbExposeMediaSetting = entityConverter.mergeExposeMediaSetting(uiExposeMediaSetting, dbExposeMediaSetting);
            service.save(dbExposeMediaSetting);
        }
        return exposeMediaSettingDTO;
    }
    @DeleteMapping("")
    void delete(@PathVariable("triggerId") Long triggerId){
        ExposeMediaSetting dbExposeMediaSetting = service.findByTriggerId(triggerId);
        if(dbExposeMediaSetting != null) {
            dbExposeMediaSetting.setStatus(false);
            service.save(dbExposeMediaSetting);
        }
    }
}
