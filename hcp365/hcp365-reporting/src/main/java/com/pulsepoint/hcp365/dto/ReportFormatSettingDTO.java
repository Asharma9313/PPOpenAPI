package com.pulsepoint.hcp365.dto;

import com.pulsepoint.hcp365.enums.ReportArchivationType;
import com.pulsepoint.hcp365.enums.ReportFileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportFormatSettingDTO {
    private Long id;
    private Long advertiserId;
    private ReportFileType fileType;
    private String fileCustomExtension;
    private String fileCustomDelimiter;
    private ReportArchivationType archivationType;
    private boolean columnHeader;
}
