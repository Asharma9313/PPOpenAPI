package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordSmartListDTO {
    private Long triggerId;
    private Integer removeAfter;
    private Long groupId;
}
