package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NPISmartListDTO {
    private Long triggerId;
    //private String smartListName;
    private Long groupId;
    private Integer removeAfter;
    private Boolean hcpRetrospective;
}
