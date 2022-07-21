package com.pulsepoint.hcp365.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportTemplateColumnDTO {
    private Long id;
    private String name;
    private int ordinal;
}
