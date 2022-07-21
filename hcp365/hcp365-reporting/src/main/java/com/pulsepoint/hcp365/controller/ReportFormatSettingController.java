package com.pulsepoint.hcp365.controller;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.dto.EntityConverter;
import com.pulsepoint.hcp365.dto.ReportFormatSettingDTO;
import com.pulsepoint.hcp365.modal.ReportFormatSetting;
import com.pulsepoint.hcp365.service.ReportFormatSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reporting/setting")
@RefreshScope
@Transactional
public class ReportFormatSettingController {
    @Autowired
    ReportFormatSettingService reportFormatSettingService;

    @Autowired
    EntityConverter entityConverter;

    @GetMapping("/advertiser/{id}")
    public ReportFormatSettingDTO getByAdvId(@PathVariable("id") Long advId) {
        ReportFormatSetting setting = reportFormatSettingService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), advId);
        return entityConverter.convertToDto(ReportFormatSettingDTO.class, setting);
    }

    @PostMapping("/advertiser/{id}")
    public ReportFormatSettingDTO createReportFormatSetting(@PathVariable("id") Long advId, @RequestBody ReportFormatSettingDTO settingDTO) {
        ReportFormatSetting setting = reportFormatSettingService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), advId);
        if (setting != null) {
            throw new InvalidDataException("Setting already exists for Advertiser");
        }
        setting = entityConverter.convertFromDto(ReportFormatSetting.class, settingDTO);
        setting.setUserId(SecurityContextUtil.getUserId());
        setting.setAccountId(SecurityContextUtil.getAccountId());
        setting = reportFormatSettingService.save(setting);
        return entityConverter.convertToDto(ReportFormatSettingDTO.class, setting);
    }

    @PutMapping("/advertiser/{id}")
    public ReportFormatSettingDTO updateReportFormatSetting(@PathVariable("id") Long advId, @RequestBody ReportFormatSettingDTO settingDTO) {
        ReportFormatSetting dbSetting = reportFormatSettingService.findByAccountIdAndAdvId(SecurityContextUtil.getAccountId(), advId);
        if (dbSetting == null) {
            throw new InvalidDataException("Setting not found");
        }
        ReportFormatSetting uiSetting = entityConverter.convertFromDto(ReportFormatSetting.class, settingDTO);
        dbSetting = entityConverter.mergeReportFormatSetting(dbSetting, uiSetting);
        dbSetting = reportFormatSettingService.save(dbSetting);
        return entityConverter.convertToDto(ReportFormatSettingDTO.class, dbSetting);
    }
}
