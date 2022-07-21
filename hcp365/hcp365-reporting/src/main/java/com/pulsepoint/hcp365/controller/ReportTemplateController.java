package com.pulsepoint.hcp365.controller;

import com.pulsepoint.hcp365.dto.EntityConverter;
import com.pulsepoint.hcp365.dto.ReportTemplateDTO;
import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.hcp365.modal.ReportTemplate;
import com.pulsepoint.hcp365.service.ReportTemplateService;
import com.pulsepoint.commons.utils.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reporting/template")
@RefreshScope
@Transactional
public class ReportTemplateController {

    @Autowired
    ReportTemplateService reportTemplateService;


    @Autowired
    EntityConverter entityConverter;

/*    @GetMapping("/tes")
    public ReportTemplateDTO testMsg(){
        //org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal p = (org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportTemplate reportTemplate = reportTemplateService.getReportTemplate(SecurityContextUtil.getAccountId(), 1L);
        return entityConverter.convertToDto(ReportTemplateDTO.class, reportTemplate);
        //ReportParamDTO dto = new ReportParamDTO(1L, "ghjghj");
        //return dto;
    }*/

    @GetMapping("/advertiser/{id}")
    public ReportTemplateDTO getByAdvId(@PathVariable("id") Long advId){
        ReportTemplate reportTemplate = reportTemplateService.getReportTemplate(SecurityContextUtil.getAccountId(),  advId);
        return entityConverter.convertToReportTemplateDTO(reportTemplate);
    }

    @PostMapping("/advertiser/{advId}")
    public ReportTemplateDTO createNewReportTemplate(@PathVariable("advId") Long advId, @RequestBody ReportTemplateDTO reportTemplateDTO){
        ReportTemplate existingTemplate = reportTemplateService.getReportTemplate(SecurityContextUtil.getAccountId(),  reportTemplateDTO.getAdvertiserId());
        if(existingTemplate != null){
            throw new InvalidDataException("Template already exists for the Advertiser");
        }
        ReportTemplate reportTemplate = entityConverter.convertFromReportTemplateDTO(reportTemplateDTO);
        reportTemplate.setUserId(SecurityContextUtil.getUserId());
        reportTemplate.setAccountId(SecurityContextUtil.getAccountId());
        reportTemplate = reportTemplateService.save(reportTemplate);
        return entityConverter.convertToReportTemplateDTO(reportTemplate);
    }

    @PutMapping("/advertiser/{advId}")
    public ReportTemplateDTO updateReportTemplate(@PathVariable("advId") Long advId,@RequestBody ReportTemplateDTO reportTemplateDTO) {
        ReportTemplate dbTemplate = reportTemplateService.getReportTemplate(SecurityContextUtil.getAccountId(), reportTemplateDTO.getAdvertiserId());
        ReportTemplate uiTemplate = entityConverter.convertFromReportTemplateDTO(reportTemplateDTO);
        if (dbTemplate == null) {
            throw new InvalidDataException("Template not found for the Advertiser");
        }
        dbTemplate = entityConverter.mergeReportTemplateChanges(dbTemplate, uiTemplate);
        dbTemplate = reportTemplateService.save(dbTemplate);
        return entityConverter.convertToReportTemplateDTO(dbTemplate);
    }
}
