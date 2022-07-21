package com.pulsepoint.hcp365.service;


import com.pulsepoint.hcp365.modal.ReportTemplateColumnGroup;
import com.pulsepoint.hcp365.repository.ReportTemplateColumnGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportTemplateColumnGroupService {

    @Autowired
    ReportTemplateColumnGroupRepository repository;

    public List<ReportTemplateColumnGroup> getAll(){
        return repository.findByStatusOrderByOrdinal(true);
    }
}

