package com.pulsepoint.hcp365.trigger.dto;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickSearchAdSettingDTO {
    private Operator customKeywordQueryOperator;
    private List<Long> collectionIds;
    private List<String> keywords;
    private String customUrlParamName;
    private String customUrlParamValue;
    private Long triggerId;
}
