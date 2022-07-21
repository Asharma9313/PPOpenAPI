package com.pulsepoint.hcp365.dto;

import com.pulsepoint.hcp365.enums.ReportPeriodicalType;
import com.pulsepoint.hcp365.enums.ScheduledReportFrequencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledReportDTO {
    private Long id;
    private String name;
    private Long customDestinationId;
    private ScheduledReportFrequencyType frequencyId;
    private String FilePath;
    private String fileName;
    private Long accountId;
    private Long advId;
    private Long collectionId;
    private boolean status;
    private String scheduleStartDate;
    private String scheduleEndDate;
    private int generateReportOnDayId;
    private String generateReportOnTime;
    private int periodId;
    private Integer periodicalNumber;
    private ReportPeriodicalType periodicalNumberType;
    private int timezoneId;
    private List<Long> collectionIds;
    private List<LookupDTO> collections = new ArrayList<>();
}
