package com.pulsepoint.journey.audience.controller;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.journey.audience.dto.AudienceDefinitionDTO;
import com.pulsepoint.journey.audience.dto.LifeAudienceDefinitionDTO;
import com.pulsepoint.journey.audience.dto.LifeAudienceDefinitionSearchResultsDTO;
import com.pulsepoint.journey.audience.dto.EntityConverter;
import com.pulsepoint.journey.audience.modal.AudienceDefinition;
import com.pulsepoint.journey.audience.modal.LifeAudienceDefinition;
import com.pulsepoint.journey.audience.modal.Scorecard;
import com.pulsepoint.journey.audience.service.LifeAudienceDefinitionService;
import com.pulsepoint.journey.audience.service.ScorecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "life/audience")
@RefreshScope
@Transactional
public class LifeAudienceController {
    @Autowired
    LifeAudienceDefinitionService lifeAudienceDefinitionService;

    @Autowired
    EntityConverter entityConverter;
    @Autowired
    ScorecardService scorecardService;

    @GetMapping("/{audienceId}")
    public LifeAudienceDefinitionDTO getLifeAudienceDefinition(@PathVariable("audienceId") Long audienceId) {
        AudienceDefinition audienceDefinition = lifeAudienceDefinitionService.getById(audienceId);
        lifeAudienceDefinitionService.validateAudiencebyAccount(audienceDefinition, SecurityContextUtil.getAccountId());
        return entityConverter.convertToLifeAudienceDefinitionDTO(audienceDefinition);
    }

    @GetMapping("")
    public LifeAudienceDefinitionSearchResultsDTO searchLifeAudienceDefinitions(@RequestParam Map<String, String> queryParameters) {
        Page<LifeAudienceDefinition> audienceDefinitionPage = lifeAudienceDefinitionService.search(SecurityContextUtil.getAccountId(), queryParameters.get("name"), Integer.parseInt(queryParameters.get("pageNo")));
        LifeAudienceDefinitionSearchResultsDTO dto = new LifeAudienceDefinitionSearchResultsDTO();
        dto.setPageCount(audienceDefinitionPage.getTotalPages());
        dto.setTotalItems(audienceDefinitionPage.getTotalElements());
        if (audienceDefinitionPage.get().count() > 0) {
            dto.setAudienceDefinitionDTOS(audienceDefinitionPage.get().map(audienceDefinition -> entityConverter.convertToLifeAudienceDefinitionDTO(audienceDefinition)).collect(Collectors.toList()));
        }
        return dto;
    }

    @PostMapping("")
    public LifeAudienceDefinitionDTO saveLifeAudience(@RequestBody LifeAudienceDefinitionDTO audienceDefinitionDTO) {
        LifeAudienceDefinition audienceDefinition = entityConverter.convertFromDTO(audienceDefinitionDTO);
        audienceDefinition.setActive(true);
        if (CollectionUtils.isEmpty(audienceDefinition.getAudienceConditions()) == false) {
            audienceDefinition.getAudienceConditions().forEach(condition -> condition.setActive(true));
        }
        audienceDefinition.getAudienceRecordingPixelXRefs().forEach(pixelXref -> pixelXref.setActive(true));
        audienceDefinition.setAccountId(SecurityContextUtil.getAccountId());
        lifeAudienceDefinitionService.saveOrUpdate(audienceDefinition);
        return entityConverter.convertToLifeAudienceDefinitionDTO(audienceDefinition);
    }

    @PutMapping("/{audienceId}")
    public LifeAudienceDefinitionDTO updateLifeAudience(@PathVariable("audienceId") Long audienceId, @RequestBody LifeAudienceDefinitionDTO audienceDefinitionDTO) {
        LifeAudienceDefinition audienceDefinitionUI = entityConverter.convertFromDTO(audienceDefinitionDTO);
        LifeAudienceDefinition audienceDefinitionDB = (LifeAudienceDefinition) lifeAudienceDefinitionService.getById(audienceId);
        audienceDefinitionDB = entityConverter.mergeLifeAudienceDefinition(audienceDefinitionDB, audienceDefinitionUI);
        lifeAudienceDefinitionService.saveOrUpdate(audienceDefinitionDB);
        return entityConverter.convertToLifeAudienceDefinitionDTO(audienceDefinitionDB);
    }

    @DeleteMapping("/{audienceId}")
    public void deleteLifeAudience(@PathVariable("audienceId") Long audienceId) {
        LifeAudienceDefinition audienceDefinitionDB = lifeAudienceDefinitionService.getById(audienceId);
        lifeAudienceDefinitionService.validateAudiencebyAccount(audienceDefinitionDB, SecurityContextUtil.getAccountId());
        audienceDefinitionDB.setActive(false);
        audienceDefinitionDB.getAudienceRecordingPixelXRefs().stream().forEach(p -> p.setActive(false));
        if (CollectionUtils.isEmpty(audienceDefinitionDB.getAudienceConditions()) == false) {
            audienceDefinitionDB.getAudienceConditions().stream().forEach(cond -> cond.setActive(false));
        }
        lifeAudienceDefinitionService.saveOrUpdate(audienceDefinitionDB);
    }

    @PutMapping("/bulk")
    public List<AudienceDefinitionDTO> updateBulk(List<AudienceDefinitionDTO> audienceDefinitionDTOS) {
        audienceDefinitionDTOS.stream().forEach(audienceDefinitionDTO -> {
            LifeAudienceDefinition audienceDefinition = lifeAudienceDefinitionService.getById(audienceDefinitionDTO.getId());
            lifeAudienceDefinitionService.validateAudiencebyAccount(audienceDefinition, SecurityContextUtil.getAccountId());
            audienceDefinition.setUrlMask(audienceDefinitionDTO.getUrlMask());
            audienceDefinition.setFrequency(audienceDefinitionDTO.getFrequency());
            audienceDefinition.setRecency(audienceDefinitionDTO.getRecency());
            lifeAudienceDefinitionService.saveOrUpdate(audienceDefinition);
        });
        return audienceDefinitionDTOS;
    }

    @GetMapping("/scorecard/{audienceId}")
    public Scorecard findAll(@PathVariable("audienceId") Long audienceId) {
        return scorecardService.find(audienceId);
    }
}
