package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.dto.ClickMediaSettingDTO;
import com.pulsepoint.hcp365.trigger.modal.ClickMediaSetting;
import com.pulsepoint.hcp365.trigger.service.ClickMediaSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/clickmedia")
@RefreshScope
public class ClickMediaSettingController {

    @Autowired
    ClickMediaSettingService service;

    @Autowired
    EntityConverter entityConverter;


    @GetMapping("")
    ClickMediaSettingDTO geByTriggerId(@PathVariable("triggerId") Long triggerId){
        ClickMediaSetting clickMediaSetting = service.findByTriggerId(triggerId);
        if(clickMediaSetting != null && clickMediaSetting.getStatus() == true) {
            return entityConverter.convertToClickMediaSettingDTO(clickMediaSetting);
        } else{
            return null;
        }
    }

    @PostMapping("")
    ClickMediaSettingDTO createNew(@PathVariable("triggerId") Long triggerId, @RequestBody ClickMediaSettingDTO ClickMediaSettingDTO){
        ClickMediaSetting ClickMediaSetting = entityConverter.convertFromClickMediaSettingDTO(ClickMediaSettingDTO, triggerId);
        service.save(ClickMediaSetting);
        return ClickMediaSettingDTO;
    }

    @PutMapping("")
    ClickMediaSettingDTO update(@PathVariable("triggerId") Long triggerId, @RequestBody ClickMediaSettingDTO ClickMediaSettingDTO){
        ClickMediaSetting uiClickMediaSetting = entityConverter.convertFromClickMediaSettingDTO(ClickMediaSettingDTO, triggerId);
        ClickMediaSetting dbClickMediaSetting = service.findByTriggerId(triggerId);
        if(dbClickMediaSetting == null){
            service.save(uiClickMediaSetting);
        } else {
            dbClickMediaSetting = entityConverter.mergeClickMediaSetting(uiClickMediaSetting, dbClickMediaSetting);
            service.save(dbClickMediaSetting);
        }
        return ClickMediaSettingDTO;
    }

    @DeleteMapping("")
    void delete(@PathVariable("triggerId") Long triggerId){
        ClickMediaSetting dbClickMediaSetting = service.findByTriggerId(triggerId);
        if(dbClickMediaSetting != null) {
            dbClickMediaSetting.setStatus(false);
            service.save(dbClickMediaSetting);
        }
    }
}
