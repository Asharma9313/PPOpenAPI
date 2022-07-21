package com.pulsepoint.hcp365.service;

import lombok.Data;

@Data
public class ReportRequestDestination {
    private Long id;
    private String file_path;
    private String file_name;
    private ReportRequestDestinationControlFields control_fields;
}
