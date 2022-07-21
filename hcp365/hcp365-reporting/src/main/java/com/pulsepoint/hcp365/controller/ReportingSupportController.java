package com.pulsepoint.hcp365.controller;



import com.pulsepoint.hcp365.dto.EntityConverter;
import com.pulsepoint.hcp365.dto.ReportTemplateColumnDTO;
import com.pulsepoint.hcp365.dto.ReportTemplateColumnGroupDTO;
import com.pulsepoint.hcp365.modal.ReportTemplateColumnGroup;
import com.pulsepoint.hcp365.service.ReportTemplateColumnGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/reporting/support")
@RefreshScope
@Transactional
public class ReportingSupportController {
    @Autowired
    ReportTemplateColumnGroupService reportTemplateColumnGroupService;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("/template/columngroup")
    public List<ReportTemplateColumnGroupDTO> reportTemplateColumnGroups(){
        List<ReportTemplateColumnGroup> columnGroups = reportTemplateColumnGroupService.getAll();
        List<ReportTemplateColumnGroupDTO> columnGroupDTOS = new ArrayList<>();
        columnGroups.stream().forEach(columnGroup -> {
            ReportTemplateColumnGroupDTO columnGroupDTO = entityConverter.convertToDto(ReportTemplateColumnGroupDTO.class, columnGroup);
            columnGroupDTO.setReportTemplateColumnList(new ArrayList<>());
            columnGroup.getReportTemplateColumnList().stream().forEach(column -> {
                columnGroupDTO.getReportTemplateColumnList().add(entityConverter.convertToDto(ReportTemplateColumnDTO.class, column));
            });
            columnGroupDTOS.add(columnGroupDTO);
        });
        return columnGroupDTOS;
    }

}
