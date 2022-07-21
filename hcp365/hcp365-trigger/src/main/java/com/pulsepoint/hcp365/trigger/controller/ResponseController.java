package com.pulsepoint.hcp365.trigger.controller;

import com.pulsepoint.hcp365.trigger.dto.NPISmartListDTO;
import com.pulsepoint.hcp365.trigger.dto.ResponseDTO;
import com.pulsepoint.hcp365.trigger.modal.NPISmartList;
import com.pulsepoint.hcp365.trigger.service.NPISmartListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/trigger/{triggerId}/response/all")
@RefreshScope
public class ResponseController {

    @Autowired
    NPISmartListController npiSmartListController;

    @Autowired
    KeywordSmartListController keywordSmartListController;

    @Autowired
    WebhookController webhookController;

    @GetMapping("")
    public ResponseDTO getAllResponses(@PathVariable("triggerId") Long triggerId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setNpiSmartList(npiSmartListController.getByTriggerId(triggerId));
        responseDTO.setKeywordSmartList(keywordSmartListController.getByTriggerId(triggerId));
        responseDTO.setWebhook(webhookController.getByTriggerId(triggerId));
        return responseDTO;
    }

    @PutMapping("")
    public ResponseDTO update(@PathVariable("triggerId") Long triggerId, @RequestBody ResponseDTO responseDTO){
        if(responseDTO.getNpiSmartList() != null){
            npiSmartListController.save(triggerId, responseDTO.getNpiSmartList());
        } else{
            npiSmartListController.delete(triggerId);
        }
        if(responseDTO.getKeywordSmartList() != null){
            keywordSmartListController.save(triggerId, responseDTO.getKeywordSmartList());
        } else{
            keywordSmartListController.delete(triggerId);
        }
        if(responseDTO.getWebhook() != null){
            webhookController.save(triggerId, responseDTO.getWebhook());
        } else{
            webhookController.delete(triggerId);
        }
        return responseDTO;
    }

    @GetMapping("/status")
    public boolean getResponsesStatus(@PathVariable("triggerId") Long triggerId){
        if(npiSmartListController.getByTriggerId(triggerId) != null || keywordSmartListController.getByTriggerId(triggerId) != null || webhookController.getByTriggerId(triggerId) != null){
            return true;
        }
        return false;
    }

    public List<ResponseDTO> getNPISmartListResponses(List<Long> triggerIds){
        List<ResponseDTO> responseDTOS = new ArrayList<>();
        List<NPISmartListDTO> smartListDTOS = npiSmartListController.getByTriggerIds(triggerIds);
        smartListDTOS.stream().forEach(smartListDTO -> {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setNpiSmartList(smartListDTO);
            responseDTOS.add(responseDTO);
        });
        return responseDTOS;
    }
}
