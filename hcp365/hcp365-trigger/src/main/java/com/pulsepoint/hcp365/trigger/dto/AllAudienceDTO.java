package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAudienceDTO {
    private List<AudienceDTO> npiLists;
    private List<AudienceDTO> hcpTargets;
    private Long triggerId;
}
