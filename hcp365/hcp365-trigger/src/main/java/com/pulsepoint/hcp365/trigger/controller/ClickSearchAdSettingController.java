package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.ClickSearchAdSettingDTO;
import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSetting;
import com.pulsepoint.hcp365.trigger.service.ClickSearchAdSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/clicksearchad")
@RefreshScope
public class ClickSearchAdSettingController {
    @Autowired
    ClickSearchAdSettingService service;


    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    public ClickSearchAdSettingDTO getByTriggerId(@PathVariable("triggerId") Long triggerId) {
        ClickSearchAdSetting clickSearchAdSetting = service.findByTriggerId(triggerId);
        if(clickSearchAdSetting!= null && clickSearchAdSetting.getStatus() == true) {
            return entityConverter.convertToClickSearchAdSettingDTO(service.findByTriggerId(triggerId));
        }
        return null;
    }

    @PostMapping("")
    public ClickSearchAdSettingDTO createNew(@PathVariable("triggerId") Long triggerId, @RequestBody ClickSearchAdSettingDTO clickSearchAdSettingDTO){
        ClickSearchAdSetting dbClickSearchAdSetting = service.findByTriggerId(triggerId);
        if(dbClickSearchAdSetting == null) {
            ClickSearchAdSetting clickSearchAdSetting = entityConverter.convertFromClickSearchAdSettingDTO(clickSearchAdSettingDTO, triggerId);
            service.saveClickSearchAdSetting(clickSearchAdSetting);
        } else{
            update(triggerId, clickSearchAdSettingDTO);
        }
        return clickSearchAdSettingDTO;
    }

    @PutMapping("")
    public ClickSearchAdSettingDTO update(@PathVariable("triggerId") Long triggerId, @RequestBody ClickSearchAdSettingDTO clickSearchAdSettingDTO) {
        ClickSearchAdSetting uiClickSearchAdSetting = entityConverter.convertFromClickSearchAdSettingDTO(clickSearchAdSettingDTO, triggerId);
        ClickSearchAdSetting dbClickSearchAdSetting = service.findByTriggerId(triggerId);
        if(dbClickSearchAdSetting == null){
            service.saveClickSearchAdSetting(uiClickSearchAdSetting);
        } else {
            dbClickSearchAdSetting = entityConverter.mergeClickSearchAdSetting(uiClickSearchAdSetting, dbClickSearchAdSetting);
            service.saveClickSearchAdSetting(dbClickSearchAdSetting);
        }
        return clickSearchAdSettingDTO;
    }

    @DeleteMapping("")
    void delete(@PathVariable("triggerId") Long triggerId){
        ClickSearchAdSetting dbClickSearchAdSetting = service.findByTriggerId(triggerId);
        if(dbClickSearchAdSetting != null) {
            dbClickSearchAdSetting.setStatus(false);
            service.saveClickSearchAdSetting(dbClickSearchAdSetting);
        }
    }

    public List<ClickSearchAdSettingDTO> getByTriggerIds(List<Long> triggerIds){
        List<ClickSearchAdSetting> searchAdSettings = service.getByTriggerIds(triggerIds);
        if(CollectionUtils.isEmpty(searchAdSettings) == false){
            List<ClickSearchAdSettingDTO> clickSearchAdSettingDTOList = new ArrayList<>();
            searchAdSettings.stream().filter(c -> c.getStatus() == true).forEach(searchAdSetting -> {
                clickSearchAdSettingDTOList.add(entityConverter.convertToClickSearchAdSettingDTO(searchAdSetting));
            });
            return clickSearchAdSettingDTOList;
        } else{
            return null;
        }
    }
}
