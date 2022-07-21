package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.modal.ClickEmailSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.service.ClickEmailSettingCollectionRefService;
import com.pulsepoint.hcp365.trigger.service.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/clickemail")
@RefreshScope
public class ClickEmailSettingController {
    @Autowired
    ClickEmailSettingCollectionRefService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    List<Long> getByTriggerId(@PathVariable("triggerId") Long triggerId){
        List<ClickEmailSettingCollectionRef> refs= service.findByTriggerId(triggerId);
        if(!CollectionUtils.isEmpty(refs)){
            refs = refs.stream().filter(r -> r.getStatus() == true).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(refs)) {
                return refs.stream().filter(r -> r.getStatus() == true).map(r -> r.getCollectionId()).collect(Collectors.toList());
            }
        }
        return null;
    }

    @PutMapping("")
    List<Long> update(@PathVariable("triggerId") Long triggerId, @RequestBody List<Long> collectionIds){
        List<ClickEmailSettingCollectionRef> dbRefs= service.findByTriggerId(triggerId);
        dbRefs = entityConverter.mergeClickEmailSettingCollectionRefs(collectionIds, dbRefs, triggerId);
        if(dbRefs.size() > 0){
            service.save(dbRefs);
        }
        return collectionIds;
    }

    @DeleteMapping("")

    void delete(@PathVariable("triggerId") Long triggerId){
        List<ClickEmailSettingCollectionRef> dbRefs= service.findByTriggerId(triggerId);
        if(dbRefs != null) {
            dbRefs.stream().forEach(dbRef -> dbRef.setStatus(false));
            service.save(dbRefs);
        }
    }
}
