package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.modal.ReportTemplate;
import com.pulsepoint.hcp365.repository.ReportTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportTemplateService {

    @Autowired
    ReportTemplateRepository reportTemplateRepository;


     public ReportTemplate getReportTemplate(Long accountId, Long advertiserId){
         return reportTemplateRepository.findByAccountIdAndAdvertiserIdAndStatus(accountId, advertiserId, true);
     }

     public ReportTemplate save(ReportTemplate reportTemplate){
         return reportTemplateRepository.save(reportTemplate);
     }


}
