package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerDTOFull {
    private TriggerDTO triggerDTO;
    private ActionDTO actionDTO;
    private ResponseDTO responseDTO;
    private AllAudienceDTO allAudienceDTO;
}
