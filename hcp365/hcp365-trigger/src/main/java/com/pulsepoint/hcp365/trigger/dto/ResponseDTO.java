package com.pulsepoint.hcp365.trigger.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    NPISmartListDTO npiSmartList;
    KeywordSmartListDTO keywordSmartList;
    WebhookDTO webhook;
}
