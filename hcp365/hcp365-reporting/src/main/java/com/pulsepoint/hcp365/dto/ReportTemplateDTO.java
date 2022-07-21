package com.pulsepoint.hcp365.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTemplateDTO {
    private Long id;
    private String name;
    private Boolean status;
    private Long advertiserId;
    private List<ReportTemplateColumnDefinitionDTO> reportTemplateColumnDefinitionDTOS;
}
