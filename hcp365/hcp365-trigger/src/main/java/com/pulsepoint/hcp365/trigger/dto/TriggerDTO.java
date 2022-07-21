package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerDTO {
    private Long id;
    private String name;
    private Long advId;
    private boolean fromLife;
    private String comment;
    private boolean active;
}
