package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.modal.OpenEmailSettingCollectionRef;
import com.pulsepoint.hcp365.trigger.service.OpenEmailSettingCollectionRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/openemail")
@RefreshScope
public class OpenEmailSettingController {
    @Autowired
    OpenEmailSettingCollectionRefService service;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("")
    List<Long> getByTriggerId(@PathVariable("triggerId") Long triggerId){
        List<OpenEmailSettingCollectionRef> refs= service.findByTriggerId(triggerId);
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
        List<OpenEmailSettingCollectionRef> dbRefs= service.findByTriggerId(triggerId);
        dbRefs = entityConverter.mergeOpenEmailSettingCollectionRefs(collectionIds, dbRefs, triggerId);
        if(dbRefs.size() > 0){
            service.save(dbRefs);
        }
        return collectionIds;
    }

    @DeleteMapping("")
    void delete(@PathVariable("triggerId") Long triggerId){
        List<OpenEmailSettingCollectionRef> dbRefs= service.findByTriggerId(triggerId);
        if(dbRefs != null) {
            dbRefs.stream().forEach(dbRef -> {
                dbRef.setStatus(false);
            });
            service.save(dbRefs);
        }
    }
}
