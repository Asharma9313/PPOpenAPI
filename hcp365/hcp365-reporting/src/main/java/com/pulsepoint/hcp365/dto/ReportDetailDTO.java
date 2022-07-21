package com.pulsepoint.hcp365.dto;

import com.pulsepoint.hcp365.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDTO {
    private Long id;
    private String requestId;
    private String fileName;
    private String processDescription;
    private ReportStatus status;
    private List<LookupDTO> collections = new ArrayList<>();
}
