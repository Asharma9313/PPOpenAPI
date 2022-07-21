package com.pulsepoint.hcp365.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ReportRequestV2 {
    private Long report_id;
    private int version = 1;
    private List<ReportRequestColumn> select;
    private String[] where;
    private String report_type;
    private String data_source;
    private String result_callback;
    private String file_format;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String compression_type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReportRequestDestination destination;
    private String environment;
}
