package com.pulsepoint.hcp365.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailCollectionDTO {
    private List<ReportDetailDTO> reportDetailsList;
    private int totalReports;
}
