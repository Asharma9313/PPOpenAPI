package com.pulsepoint.hcp365.dto;

import com.pulsepoint.hcp365.enums.ReportTemplateFieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTemplateColumnDefinitionDTO {
    private Long id;
    private ReportTemplateFieldType fieldType;
    private Long fieldRefId;
    private String customFieldName;
    private int ordinal;
    private boolean status;
}
