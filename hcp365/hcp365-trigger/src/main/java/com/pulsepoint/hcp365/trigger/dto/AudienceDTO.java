package com.pulsepoint.hcp365.trigger.dto;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudienceDTO {
    private Long id;
    private Operator operator;
    private String name;
}
