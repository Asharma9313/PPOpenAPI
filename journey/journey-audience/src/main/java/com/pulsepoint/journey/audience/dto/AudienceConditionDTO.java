package com.pulsepoint.journey.audience.dto;

import com.pulsepoint.journey.audience.enums.AudienceConditionType;
import com.pulsepoint.journey.audience.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudienceConditionDTO {
    private Long id;
    private AudienceConditionType conditionType;;
    private Operator operator;
    private String conditionValue;
}
