package com.pulsepoint.hcp365.trigger.dto;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitBrandPageSettingDTO {
    private Integer frequencyControlValue;
    private String customUrlParamName;
    private String customUrlParamValue;
    private Operator urlFilterOperator;
    private List<Long> collectionIds;
    private List<String> urlVisibleFilters;
    private List<String> urlIgnoredFilters;
    private Long triggerId;
}
