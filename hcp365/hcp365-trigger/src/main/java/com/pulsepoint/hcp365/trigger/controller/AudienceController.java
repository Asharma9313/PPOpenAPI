package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.dto.AllAudienceDTO;
import com.pulsepoint.hcp365.trigger.dto.AudienceDTO;
import com.pulsepoint.hcp365.trigger.dto.EntityConverter;
import com.pulsepoint.hcp365.trigger.enums.AudienceType;
import com.pulsepoint.hcp365.trigger.modal.Audience;
import com.pulsepoint.hcp365.trigger.modal.HCPSpecialtyCategory;
import com.pulsepoint.hcp365.trigger.modal.NPIGroup;
import com.pulsepoint.hcp365.trigger.service.AudienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/audience")
@RefreshScope
public class AudienceController {
    @Autowired
    AudienceService audienceService;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("/type/{audienceType}")
    public List<AudienceDTO> getAudienceData(@PathVariable("triggerId") Long triggerId, @PathVariable("audienceType") String audienceTypeVal) {
        AudienceType audienceType = AudienceType.valueOf(audienceTypeVal);
        List<Audience> audienceList = audienceService.findActiveAudience(triggerId, audienceType);
        List<AudienceDTO> audienceDTOS = entityConverter.convertToAudienceDTOList(audienceList);
        if(!CollectionUtils.isEmpty(audienceDTOS)){
            if(audienceType == AudienceType.NPILIST) {
                List<NPIGroup> npiGroups = audienceService.findNPIGroupsByIds(audienceDTOS.stream().map(a -> a.getId()).collect(Collectors.toList()), SecurityContextUtil.getAccountId());
                npiGroups.stream().forEach(npiGroup -> {
                    audienceDTOS.stream().filter(a -> a.getId().equals(npiGroup.getId())).findFirst().get().setName(npiGroup.getName());
                });
            } else if(audienceType == AudienceType.HCPBYSPECIALITY){
                List<HCPSpecialtyCategory> hcpSpecialtyCategories = audienceService.findHCPSpecalitiesByIds(audienceDTOS.stream().map(a -> a.getId()).collect(Collectors.toList()));
                hcpSpecialtyCategories.stream().forEach(hcpSpecialtyCategory -> {
                    audienceDTOS.stream().filter(a -> a.getId().equals(hcpSpecialtyCategory.getGroupId())).findFirst().get().setName(hcpSpecialtyCategory.getName());
                });
            }
        }
        return audienceDTOS;
    }

    @PutMapping("/type/{audienceType}")
    public List<AudienceDTO> setAudienceTargets(@PathVariable("triggerId") Long triggerId, @PathVariable("audienceType") String audienceTypeVal, @RequestBody List<AudienceDTO> audienceDTOList) {
        AudienceType audienceType = AudienceType.valueOf(audienceTypeVal);
        if(audienceType.equals(AudienceType.NPILIST) && !CollectionUtils.isEmpty(audienceDTOList)){
            this.audienceService.validateNPIList(audienceDTOList.stream().map(a -> a.getId()).collect(Collectors.toList()), SecurityContextUtil.getAccountId());
        }
        List<Audience> dbAudienceList = audienceService.findAllAudience(triggerId, audienceType );
        List<Audience> uiAudienceList = entityConverter.convertFromAudienceDTOList(audienceDTOList, audienceType, triggerId);
        if (CollectionUtils.isEmpty(dbAudienceList)) {
            dbAudienceList = new ArrayList<>();
        }
        dbAudienceList = entityConverter.mergeAudienceList(uiAudienceList, dbAudienceList);
        audienceService.save(dbAudienceList);
        return audienceDTOList;
    }
    public List<AllAudienceDTO> getAllAudiencesbyTriggerIds(List<Long> triggerIds){
        List<AllAudienceDTO> allAudienceDTOS = new ArrayList<>();
        triggerIds.stream().forEach(triggerId -> {
            AllAudienceDTO allAudienceDTO = new AllAudienceDTO();
            allAudienceDTO.setTriggerId(triggerId);
            List<Audience> audienceList = audienceService.findActiveAudience(triggerId, AudienceType.NPILIST);
            if(CollectionUtils.isEmpty(audienceList) == false) {
                allAudienceDTO.setNpiLists(entityConverter.convertToAudienceDTOList(audienceList));
            }
            List<Audience> hcpList = audienceService.findActiveAudience(triggerId, AudienceType.HCPBYSPECIALITY);
            if(CollectionUtils.isEmpty(hcpList) == false) {
                allAudienceDTO.setHcpTargets(entityConverter.convertToAudienceDTOList(hcpList));
            }
            allAudienceDTOS.add(allAudienceDTO);
        });
        return allAudienceDTOS;
    }
}
