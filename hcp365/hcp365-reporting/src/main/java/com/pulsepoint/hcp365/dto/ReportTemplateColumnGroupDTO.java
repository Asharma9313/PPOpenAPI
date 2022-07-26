package com.pulsepoint.hcp365.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTemplateColumnGroupDTO implements Serializable {

    private Long id;
    private String name;
    private List<ReportTemplateColumnDTO> reportTemplateColumnList;
    private int ordinal;
}
