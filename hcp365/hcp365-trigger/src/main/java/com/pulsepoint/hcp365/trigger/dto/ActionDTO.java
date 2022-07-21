package com.pulsepoint.hcp365.trigger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDTO {
    VisitBrandPageSettingDTO visitBrandPageSettingDTO;
    ClickSearchAdSettingDTO clickSearchAdSettingDTO;
    ExposeMediaSettingDTO exposeMediaSettingDTO;
    ClickMediaSettingDTO clickMediaSettingDTO;
    List<Long> openEmailSettingCollectionIds;
    List<Long> clickEmailSettingCollectionIds;
    Long triggerId;
}
