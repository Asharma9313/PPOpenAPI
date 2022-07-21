package com.pulsepoint.journey.audience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeAudienceDefinitionSearchResultsDTO {
    private List<LifeAudienceDefinitionDTO> audienceDefinitionDTOS;
    private int pageCount;
    private Long totalItems;
}
