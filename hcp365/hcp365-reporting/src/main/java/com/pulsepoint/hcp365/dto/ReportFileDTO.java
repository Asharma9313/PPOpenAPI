package com.pulsepoint.hcp365.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportFileDTO {
    private String fileLocation;
    private Long accountId;
    private String userFileName;
}
