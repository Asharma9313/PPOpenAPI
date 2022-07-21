package com.pulsepoint.journey.audience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AudienceDefinitionDTO {
    private Long id;
    private String name;
    private Integer frequency;
    private Integer recency;
    private boolean active;
    private String urlMask;
    private List<AudienceConditionDTO> audienceConditionDTOList;
    private List<Long> recordingPixelIds;

}
