package com.pulsepoint.journey.audience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeAudienceDefinitionDTO extends AudienceDefinitionDTO{
    private String segmentName;
    private Long pixelId;
}
