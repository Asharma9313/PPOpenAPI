package com.pulsepoint.hcp365.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportLogParam {
    private String fromDate;
    private String toDate;
    private Long accountId;
    private Long userId;
    private Long advId;
    private Long customDestinationId;
    private String fileName;
    private String filePath;
    private List<Long> collectionIds;

}
