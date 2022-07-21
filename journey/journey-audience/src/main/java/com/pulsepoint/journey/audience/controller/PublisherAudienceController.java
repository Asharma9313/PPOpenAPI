package com.pulsepoint.journey.audience.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.journey.audience.dto.EntityConverter;
import com.pulsepoint.journey.audience.dto.PublisherAudienceDefinitionDTO;
import com.pulsepoint.journey.audience.dto.PublisherAudienceDefinitionSearchResultsDTO;
import com.pulsepoint.journey.audience.modal.*;
import com.pulsepoint.journey.audience.service.DSPSpendService;
import com.pulsepoint.journey.audience.service.PublisherAudienceDefinitionService;
import com.pulsepoint.journey.audience.service.ScorecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.pulsepoint.journey.audience.service.PublisherAudienceDefinitionService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "publisher/audience")
@RefreshScope
@Transactional
public class PublisherAudienceController {
    @Autowired
    PublisherAudienceDefinitionService publisherAudienceDefinitionService;

    @Autowired
    EntityConverter entityConverter;

    @Autowired
    ScorecardService scorecardService;

    @Autowired
    DSPSpendService dspSpendService;

    @GetMapping("/{audienceId}")
    public PublisherAudienceDefinitionDTO getPublisherAudienceDefinition(@PathVariable("audienceId") Long audienceId) {
        PublisherAudienceDefinition audienceDefinition =  publisherAudienceDefinitionService.getById(audienceId);
        publisherAudienceDefinitionService.validateAudiencebyAccount(audienceDefinition, SecurityContextUtil.getAccountId());
        return entityConverter.convertToPublisherAudienceDefinitionDTO(audienceDefinition);
    }
    @GetMapping("")
    public PublisherAudienceDefinitionSearchResultsDTO searchPublisherAudienceDefinitions(@RequestParam Map<String, String> queryParameters){
        Page<PublisherAudienceDefinition> audienceDefinitionPage = publisherAudienceDefinitionService.search(SecurityContextUtil.getAccountId(), queryParameters.get("name"), Integer.parseInt(queryParameters.get("pageNo")));
        PublisherAudienceDefinitionSearchResultsDTO dto = new PublisherAudienceDefinitionSearchResultsDTO();
        dto.setPageCount(audienceDefinitionPage.getTotalPages());
        dto.setTotalItems(audienceDefinitionPage.getTotalElements());
        if(audienceDefinitionPage.get().count() > 0){
            dto.setAudienceDefinitionDTOS(audienceDefinitionPage.get().map(audienceDefinition -> entityConverter.convertToPublisherAudienceDefinitionDTO(audienceDefinition)).collect(Collectors.toList()));
        }
        return dto;
    }

    @PostMapping("")
    public PublisherAudienceDefinitionDTO savePublisherAudienceDefinition(@RequestBody PublisherAudienceDefinitionDTO audienceDefinitionDTO){
        PublisherAudienceDefinition audienceDefinition = entityConverter.convertFromDTO(audienceDefinitionDTO);
        audienceDefinition.setActive(true);
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceConditions()) == false){
            audienceDefinition.getAudienceConditions().forEach(condition -> condition.setActive(true));
        }
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceDealMappingXRefs()) == false){
            audienceDefinition.getAudienceDealMappingXRefs().forEach(xref -> xref.setActive(true));
        }
        audienceDefinition.getAudienceRecordingPixelXRefs().forEach(pixelXref -> pixelXref.setActive(true));
        audienceDefinition.setAccountId(SecurityContextUtil.getAccountId());
        publisherAudienceDefinitionService.saveOrUpdate(audienceDefinition);
        return entityConverter.convertToPublisherAudienceDefinitionDTO(audienceDefinition);
    }
    @PutMapping("/{audienceId}")
    public PublisherAudienceDefinitionDTO updatePublisherAudienceDefinition(@PathVariable("audienceId") Long audienceId, @RequestBody PublisherAudienceDefinitionDTO audienceDefinitionDTO) {
        PublisherAudienceDefinition audienceDefinitionUI = entityConverter.convertFromDTO(audienceDefinitionDTO);
        PublisherAudienceDefinition audienceDefinitionDB = (PublisherAudienceDefinition) publisherAudienceDefinitionService.getById(audienceId);
        audienceDefinitionDB =entityConverter.mergePublisherAudienceDefinition(audienceDefinitionDB, audienceDefinitionUI);
        publisherAudienceDefinitionService.saveOrUpdate(audienceDefinitionDB);
        return entityConverter.convertToPublisherAudienceDefinitionDTO(audienceDefinitionDB);
    }

    @DeleteMapping("/{audienceId}")
    public void deletePublisherAudience(@PathVariable("audienceId") Long audienceId){
        PublisherAudienceDefinition audienceDefinitionDB = publisherAudienceDefinitionService.getById(audienceId);
        publisherAudienceDefinitionService.validateAudiencebyAccount(audienceDefinitionDB, SecurityContextUtil.getAccountId());
        audienceDefinitionDB.setActive(false);
        audienceDefinitionDB.getAudienceRecordingPixelXRefs().stream().forEach(p -> p.setActive(false));
        if(CollectionUtils.isEmpty(audienceDefinitionDB.getAudienceConditions()) == false){
            audienceDefinitionDB.getAudienceConditions().stream().forEach(cond -> cond.setActive(false));
        }
        publisherAudienceDefinitionService.saveOrUpdate(audienceDefinitionDB);
    }

    @GetMapping("/dsps")
    public List<DSP> getAllDSPs(){
        List<DSP> dsps= publisherAudienceDefinitionService.getAllDSPs();
        dsps.sort(Comparator.comparing(DSP::getDisplayName));
        return dsps;
    }

    @GetMapping("/scorecard/{audienceId}")
    public Scorecard getScoreCard(@PathVariable("audienceId") Long audienceId){
        return scorecardService.find(audienceId);
    }
    @GetMapping("/spend/{audienceId}")
    public List<DSPSpend> getTopBuyers(@PathVariable("audienceId") Long audienceId){
        return dspSpendService.findTop5Buyers(audienceId);
    }

}
