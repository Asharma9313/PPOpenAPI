package com.pulsepoint.journey.audience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherAudienceDealMappingXRefDTO {
    private Long id;
    private Long internalDealId;
    private Long dspId;

}
