package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookDTO {
    private String url;
    //private String npiMacro;
    private Integer requestMethodId;
    private Integer contentTypeId;
    private String requestBody;
}
