package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookTriggerContentTypeDTO {
    private Long id;
    private String name;
    private String contentType;
}
