package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.ActionDTO;
import com.pulsepoint.hcp365.trigger.dto.ClickSearchAdSettingDTO;
import com.pulsepoint.hcp365.trigger.dto.VisitBrandPageSettingDTO;
import com.pulsepoint.hcp365.trigger.modal.ClickSearchAdSetting;
import com.pulsepoint.hcp365.trigger.modal.VisitBrandPageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/action/all")
@RefreshScope
public class ActionController {
    @Autowired
    VisitBrandPageSettingController visitBrandPageSettingController;

    @Autowired
    ClickSearchAdSettingController clickSearchAdSettingController;

    @Autowired
    ExposeMediaSettingController exposeMediaSettingController;

    @Autowired
    ClickMediaSettingController clickMediaSettingController;

    @Autowired
    OpenEmailSettingController openEmailSettingController;

    @Autowired
    ClickEmailSettingController clickEmailSettingController;

    @PutMapping("")
    public ActionDTO saveActions(@PathVariable("triggerId") Long triggerId, @RequestBody ActionDTO actionDTO) {
        if (actionDTO.getVisitBrandPageSettingDTO() != null) {
            visitBrandPageSettingController.update(triggerId, actionDTO.getVisitBrandPageSettingDTO());
        } else{
            visitBrandPageSettingController.delete(triggerId);
        }
        if (actionDTO.getClickSearchAdSettingDTO() != null) {
            clickSearchAdSettingController.update(triggerId, actionDTO.getClickSearchAdSettingDTO());
        } else{
            clickSearchAdSettingController.delete(triggerId);
        }
        if (actionDTO.getExposeMediaSettingDTO() != null) {
            exposeMediaSettingController.update(triggerId, actionDTO.getExposeMediaSettingDTO());
        } else{
            exposeMediaSettingController.delete(triggerId);
        }
        if (actionDTO.getClickMediaSettingDTO() != null) {
            clickMediaSettingController.update(triggerId, actionDTO.getClickMediaSettingDTO());
        } else{
            clickMediaSettingController.delete(triggerId);
        }
        if (actionDTO.getOpenEmailSettingCollectionIds() != null) {
            openEmailSettingController.update(triggerId, actionDTO.getOpenEmailSettingCollectionIds());
        } else{
            openEmailSettingController.delete(triggerId);
        }
        if (actionDTO.getClickEmailSettingCollectionIds() != null) {
            clickEmailSettingController.update(triggerId, actionDTO.getClickEmailSettingCollectionIds());
        } else{
            clickEmailSettingController.delete(triggerId);
        }

        return actionDTO;
    }

    @GetMapping("")
    public ActionDTO getActions(@PathVariable("triggerId") Long triggerId) {
        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setVisitBrandPageSettingDTO(visitBrandPageSettingController.getByTriggerId(triggerId));
        actionDTO.setClickSearchAdSettingDTO(clickSearchAdSettingController.getByTriggerId(triggerId));
        actionDTO.setExposeMediaSettingDTO(exposeMediaSettingController.geByTriggerId(triggerId));
        actionDTO.setClickMediaSettingDTO(clickMediaSettingController.geByTriggerId(triggerId));
        actionDTO.setOpenEmailSettingCollectionIds(openEmailSettingController.getByTriggerId(triggerId));
        actionDTO.setClickEmailSettingCollectionIds(clickEmailSettingController.getByTriggerId(triggerId));
        return actionDTO;
    }
    @GetMapping("/status")
    public boolean getActionStatus(@PathVariable("triggerId") Long triggerId){
        if(visitBrandPageSettingController.getByTriggerId(triggerId) != null || clickSearchAdSettingController.getByTriggerId(triggerId) != null || exposeMediaSettingController.geByTriggerId(triggerId) != null || clickMediaSettingController.geByTriggerId(triggerId) != null || openEmailSettingController.getByTriggerId(triggerId) != null || clickEmailSettingController.getByTriggerId(triggerId) != null){
            return true;
        }
        return false;
    }

    public List<ActionDTO> getActionsByTriggerIds(List<Long> triggerIds){
        List<ActionDTO> actionDTOS = new ArrayList<>();
        //Add Visit Brnad Page Settings only for now as per the UI requirement. We might add other actions in future.
        List<VisitBrandPageSettingDTO> visitBrandPageSettingDTOS = visitBrandPageSettingController.getByTriggerIds(triggerIds);
        if(CollectionUtils.isEmpty(visitBrandPageSettingDTOS) == false) {
            visitBrandPageSettingDTOS.forEach(visitBrandPageSettingDTO -> {
                ActionDTO actionDTO = new ActionDTO();
                actionDTO.setTriggerId(visitBrandPageSettingDTO.getTriggerId());
                actionDTO.setVisitBrandPageSettingDTO(visitBrandPageSettingDTO);
                actionDTOS.add(actionDTO);
            });
        }
        List<ClickSearchAdSettingDTO> clickSearchAdSettingDTOs = clickSearchAdSettingController.getByTriggerIds(triggerIds);
        if(CollectionUtils.isEmpty(clickSearchAdSettingDTOs) == false){
            clickSearchAdSettingDTOs.stream().forEach(clickSearchAdSettingDTO ->{
                ActionDTO actionDTO = actionDTOS.stream().filter(a -> a.getTriggerId().equals(clickSearchAdSettingDTO.getTriggerId())).findFirst().orElse(null);
                if(actionDTO == null){
                    actionDTO = new ActionDTO();
                    actionDTO.setTriggerId(clickSearchAdSettingDTO.getTriggerId());
                    actionDTOS.add(actionDTO);
                }
                actionDTO.setClickSearchAdSettingDTO(clickSearchAdSettingDTO);

            });
        }
        return actionDTOS;
    }

}
