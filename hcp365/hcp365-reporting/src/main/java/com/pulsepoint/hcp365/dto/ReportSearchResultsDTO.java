package com.pulsepoint.hcp365.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSearchResultsDTO {
    private List<ReportDetailDTO> reportDetails;
    private long total;

}
