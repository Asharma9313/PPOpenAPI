package com.pulsepoint.journey.audience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherAudienceDefinitionDTO extends AudienceDefinitionDTO{
    private String dealName;
    private String dealDescription;
    private Double dealPrice;
    private List<PublisherAudienceDealMappingXRefDTO> dealMappingXRefDTOS;
}
