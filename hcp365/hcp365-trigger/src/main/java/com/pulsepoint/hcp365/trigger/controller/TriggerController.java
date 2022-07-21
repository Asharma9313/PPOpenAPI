package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.dto.*;
import com.pulsepoint.hcp365.trigger.enums.AudienceType;
import com.pulsepoint.hcp365.trigger.modal.*;
import com.pulsepoint.hcp365.trigger.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trigger")
@RefreshScope
public class TriggerController {
    @Autowired
    TriggerService triggerService;

    @Autowired
    EntityConverter entityConverter;

    @Autowired
    ActionController actionController;

    @Autowired
    ResponseController responseController;

    @Autowired
    VisitBrandPageSettingService visitBrandPageSettingService;

    @Autowired
    ClickSearchAdSettingService clickSearchAdSettingService;

    @Autowired
    AudienceController audienceController;

    @Autowired
    NPISmartListService npiSmartListService;

    @Autowired
    AudienceService audienceService;

    @Autowired
    NPIGroupService npiGroupService;

    @Autowired
    WebhookService webhookService;

    @GetMapping("/advertiser/{advId}")
    public List<TriggerDTO> getTriggers(@PathVariable("advId") Long advId, @RequestParam Map<String, String> queryParameters) {
        boolean fromLife = false;
        if(queryParameters !=null && queryParameters.containsKey("fromLife") && queryParameters.get("fromLife").equals("true")){
            fromLife = true;
        }
        List<Trigger> triggersList = triggerService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), advId, fromLife);
        return entityConverter.convertToDtoList(TriggerDTO.class, triggersList);
    }

    @PostMapping("/advertiser/{advId}")
    public TriggerDTO createNewTrigger(@RequestBody TriggerDTO triggerDTO) {
        Trigger trigger = entityConverter.convertFromDto(Trigger.class, triggerDTO);
        trigger.setActive(true);
        trigger.setAccountId(SecurityContextUtil.getAccountId());
        trigger = triggerService.save(trigger);
        return entityConverter.convertToDto(TriggerDTO.class, trigger);
    }

    @PostMapping("/life/advertiser")
    public List<TriggerDTOFull> createNewTriggerForLife(@RequestBody List<TriggerDTOFull> triggerDTOFullList) {
        triggerDTOFullList.stream().forEach(triggerDTOFull -> {
            Trigger trigger = entityConverter.convertFromDto(Trigger.class, triggerDTOFull.getTriggerDTO());
            trigger.setActive(true);
            trigger.setAccountId(SecurityContextUtil.getAccountId());
            trigger = triggerService.save(trigger);
            if (triggerDTOFull.getAllAudienceDTO() == null) {
                triggerDTOFull.setAllAudienceDTO(new AllAudienceDTO());
            }
            audienceController.setAudienceTargets(trigger.getId(), "NPILIST", triggerDTOFull.getAllAudienceDTO().getNpiLists());
            audienceController.setAudienceTargets(trigger.getId(), "HCPBYSPECIALITY", triggerDTOFull.getAllAudienceDTO().getHcpTargets());
            actionController.saveActions(trigger.getId(), triggerDTOFull.getActionDTO());
            responseController.update(trigger.getId(), triggerDTOFull.getResponseDTO());
            triggerDTOFull.setTriggerDTO(entityConverter.convertToDto(TriggerDTO.class, trigger));
        });
        return triggerDTOFullList;
    }

    @PutMapping("/life/advertiser")
    public List<TriggerDTOFull> updateTriggerForLife(@RequestBody List<TriggerDTOFull> triggerDTOFullList) {
        triggerDTOFullList.stream().forEach(triggerDTOFull -> {
            Trigger trigger = null;
            if (triggerDTOFull.getTriggerDTO().getId() != null && triggerDTOFull.getTriggerDTO().getId() > 0) {
                trigger = triggerService.findById(triggerDTOFull.getTriggerDTO().getId());
                triggerService.validateTriggersbyAccount(Arrays.asList(trigger), SecurityContextUtil.getAccountId());
                trigger.setName(triggerDTOFull.getTriggerDTO().getName());
                trigger.setActive(triggerDTOFull.getTriggerDTO().isActive());
                trigger = triggerService.save(trigger);
            } else {
                trigger = entityConverter.convertFromDto(Trigger.class, triggerDTOFull.getTriggerDTO());
                trigger.setActive(true);
                trigger.setAccountId(SecurityContextUtil.getAccountId());
                trigger = triggerService.save(trigger);
            }
            if (triggerDTOFull.getAllAudienceDTO() == null) {
                triggerDTOFull.setAllAudienceDTO(new AllAudienceDTO());
            }
            audienceController.setAudienceTargets(trigger.getId(), "NPILIST", triggerDTOFull.getAllAudienceDTO().getNpiLists());
            audienceController.setAudienceTargets(trigger.getId(), "HCPBYSPECIALITY", triggerDTOFull.getAllAudienceDTO().getHcpTargets());
            actionController.saveActions(trigger.getId(), triggerDTOFull.getActionDTO());
            responseController.update(trigger.getId(), triggerDTOFull.getResponseDTO());
            triggerDTOFull.setTriggerDTO(entityConverter.convertToDto(TriggerDTO.class, trigger));
        });
        triggerDTOFullList = triggerDTOFullList.stream().filter(t -> t.getTriggerDTO().isActive() == true).collect(Collectors.toList());
        return triggerDTOFullList;
    }

    @PutMapping("/{triggerId}")
    public TriggerDTO updateTrigger(@PathVariable("triggerId") Long triggerId, @RequestBody TriggerDTO triggerDTO) {
        triggerService.validateTriggerbyAccount(triggerId, SecurityContextUtil.getAccountId());
        Trigger dbTrigger = triggerService.findById(triggerId);
        if (dbTrigger.getAccountId().equals(SecurityContextUtil.getAccountId()) && dbTrigger.getAdvId().equals(triggerDTO.getAdvId())) {
            Trigger uiTrigger = entityConverter.convertFromDto(Trigger.class, triggerDTO);
            dbTrigger = entityConverter.mergeTrigger(uiTrigger, dbTrigger);
            triggerService.save(dbTrigger);
            return triggerDTO;
        }
        return null;
    }

    @DeleteMapping("/{triggerId}")
    public void deleteTrigger(@PathVariable("triggerId") Long triggerId){
        triggerService.validateTriggerbyAccount(triggerId, SecurityContextUtil.getAccountId());
        Trigger dbTrigger = triggerService.findById(triggerId);
        dbTrigger.setActive(false);
        triggerService.save(dbTrigger);
    }

    @PutMapping("/{triggerId}/disable")
    public void disableTrigger(@PathVariable("triggerId") Long triggerId) {
        triggerService.validateTriggerbyAccount(triggerId, SecurityContextUtil.getAccountId());
        Trigger dbTrigger = triggerService.findById(triggerId);
        dbTrigger.setActive(false);
        triggerService.save(dbTrigger);
    }

    @PutMapping("/{triggerId}/enable")
    public void enableTrigger(@PathVariable("triggerId") Long triggerId) {
        triggerService.validateTriggerbyAccount(triggerId, SecurityContextUtil.getAccountId());
        Trigger dbTrigger = triggerService.findById(triggerId);
        dbTrigger.setActive(true);
        triggerService.save(dbTrigger);
    }

    @GetMapping("/collection/{collectionId}")
    public List<TriggerDTOFull> getByPlacementId(@PathVariable("collectionId") Long collectionId) {
        List<Long> triggerIds = new ArrayList<>();
        List<VisitBrandPageSetting> visitBrandPageSettings = visitBrandPageSettingService.findByCollectionId(collectionId);
        if(CollectionUtils.isEmpty(visitBrandPageSettings) == false) {
            triggerIds = visitBrandPageSettings.stream().filter(s -> s.getStatus() == true).map(s -> s.getTriggerId()).collect(Collectors.toList());
        }
        List<ClickSearchAdSetting> clickSearchAdSettings = clickSearchAdSettingService.findByCollectionId(collectionId);
        if(CollectionUtils.isEmpty(clickSearchAdSettings) == false){
            if(triggerIds == null){
                triggerIds = new ArrayList<>();
            }
            triggerIds.addAll(clickSearchAdSettings.stream().filter(s -> s.getStatus() == true).map(s -> s.getTriggerId()).collect(Collectors.toList()));
        }
        if(CollectionUtils.isEmpty(triggerIds)){
            return null;
        }
        return getbyTriggerIds(triggerIds, false);
    }

    List<TriggerDTOFull> getbyTriggerIds(List<Long> triggerIds, boolean ignoreAccountIdValidation) {
        List<Trigger> triggers = triggerService.findAllById(triggerIds);
        triggers = triggers.stream().filter(t -> t.isActive() == true).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(triggers)){
            return null;
        }
        if(ignoreAccountIdValidation == false) {
            triggerService.validateTriggersbyAccount(triggers, SecurityContextUtil.getAccountId());
        }
        List<TriggerDTO> triggerDTOs = entityConverter.convertToDtoList(TriggerDTO.class, triggers);
        List<ResponseDTO> responseDTOS = responseController.getNPISmartListResponses(triggerIds);
        List<ActionDTO> actionDTOS = actionController.getActionsByTriggerIds(triggerIds);
        List<AllAudienceDTO> allAudienceDTOS = audienceController.getAllAudiencesbyTriggerIds(triggerIds);
        List<TriggerDTOFull> triggerDTOFullList = new ArrayList<>();
        triggerDTOs.stream().forEach(triggerDTO -> {
            TriggerDTOFull triggerDTOFull = new TriggerDTOFull();
            triggerDTOFull.setTriggerDTO(triggerDTO);
            if(CollectionUtils.isEmpty(responseDTOS) == false) {
                triggerDTOFull.setResponseDTO(responseDTOS.stream().filter(r -> r.getNpiSmartList() != null && r.getNpiSmartList().getTriggerId().equals(triggerDTO.getId())).findFirst().orElse(null));
            }
            if(CollectionUtils.isEmpty(actionDTOS) == false) {
                ActionDTO actionDTO = new ActionDTO();
                ActionDTO actionDTOWithVisitBrandPageSetting = actionDTOS.stream().filter(r -> r.getVisitBrandPageSettingDTO() != null && r.getVisitBrandPageSettingDTO().getTriggerId().equals(triggerDTO.getId())).findFirst().orElse(null);
                if(actionDTOWithVisitBrandPageSetting != null){
                    actionDTO.setVisitBrandPageSettingDTO(actionDTOWithVisitBrandPageSetting.getVisitBrandPageSettingDTO());
                }
                ActionDTO actionDTOWithClickSearchAdSettingDTO = actionDTOS.stream().filter(r -> r.getClickSearchAdSettingDTO() != null && r.getClickSearchAdSettingDTO().getTriggerId().equals(triggerDTO.getId())).findFirst().orElse(null);
                if(actionDTOWithClickSearchAdSettingDTO != null){
                    actionDTO.setClickSearchAdSettingDTO(actionDTOWithClickSearchAdSettingDTO.getClickSearchAdSettingDTO());
                }
                triggerDTOFull.setActionDTO(actionDTO);
            }
            if(CollectionUtils.isEmpty(allAudienceDTOS) == false) {
                triggerDTOFull.setAllAudienceDTO(allAudienceDTOS.stream().filter(a -> a.getTriggerId() != null && a.getTriggerId().equals(triggerDTO.getId())).findFirst().orElse(null));
            }
            triggerDTOFullList.add(triggerDTOFull);
        });
        return triggerDTOFullList;
    }

    @GetMapping("/npiGroup/{npiGroupId}")
    public List<TriggerDTOFull> getByNPIGroupId(@PathVariable("npiGroupId") Long npiGroupId) {
        List<NPIGroup> npiGroups = npiGroupService.findNPIGroupsByIds(Arrays.asList(npiGroupId), SecurityContextUtil.getAccountId());
        if(CollectionUtils.isEmpty(npiGroups) == false) {
            List<NPISmartList> npiSmartLists = npiSmartListService.findByGroupId(npiGroupId);
            if (CollectionUtils.isEmpty(npiSmartLists)) {
                return null;
            }
            List<Long> triggerIds = npiSmartLists.stream().map(npiSmartList -> npiSmartList.getTriggerId()).collect(Collectors.toList());
            return getbyTriggerIds(triggerIds, true);
        }
        return null;
    }
    @GetMapping("/audience/npiGroup/{npiGroupId}")
    public List<TriggerDTOFull> getByAudienceTargetingNPIGroupId(@PathVariable("npiGroupId") Long npiGroupId) {
        List<Audience> audienceList = audienceService.findActiveByByAudienceTypeAndAndAudienceValue(AudienceType.NPILIST, npiGroupId);
        if (CollectionUtils.isEmpty(audienceList)) {
            return null;
        }
        List<Long> triggerIds = audienceList.stream().map(audience -> audience.getTriggerId()).collect(Collectors.toList());
        return getbyTriggerIds(triggerIds, false);
    }

    @GetMapping("/webhookTriggerRequestMethods")
    public List<WebhookTriggerRequestMethodDTO> getWebhookTriggerRequestMethods() {
        List<WebhookTriggerRequestMethod> webhookTriggerRequestMethodList = webhookService.getWebhookTriggerRequestMethods();
        return entityConverter.convertToDtoList(WebhookTriggerRequestMethodDTO.class, webhookTriggerRequestMethodList);
    }

    @GetMapping("/webhookTriggerContentTypes")
    public List<WebhookTriggerContentTypeDTO> getWebhookTriggerContentTypes() {
        List<WebhookTriggerContentType> webhookTriggerContentTypeList = webhookService.getWebhookTriggerContentTypes();
        return entityConverter.convertToDtoList(WebhookTriggerContentTypeDTO.class, webhookTriggerContentTypeList);
    }
}
