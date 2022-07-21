package com.pulsepoint.hcp365.trigger.dto;

import com.pulsepoint.hcp365.trigger.enums.AudienceType;
import com.pulsepoint.hcp365.trigger.enums.Operator;
import com.pulsepoint.hcp365.trigger.modal.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Component
public class EntityConverter {

    @Autowired
    private ModelMapper modelMapper;

    public <F, T> T convertToDto(Class<T> clazz, F fromObject) {
        if (fromObject == null) {
            return null;
        }
        return modelMapper.map(fromObject, clazz);
    }

    public <F, T> List<T> convertToDtoList(Class<T> clazz, List<F> fromObjects) {
        List<T> dtos = new ArrayList<>();
        for (F fromObject : fromObjects) {
            if (nonNull(fromObject))
                dtos.add(convertToDto(clazz, fromObject));
        }
        return dtos;
    }

    public <F, T> T convertFromDto(Class<T> clazz, F fromObject) {
        if (fromObject == null) {
            return null;
        }
        return modelMapper.map(fromObject, clazz);
    }

    public Trigger mergeTrigger(Trigger uiTrigger, Trigger dbTrigger) {
        dbTrigger.setName(uiTrigger.getName());
        return dbTrigger;
    }

    public List<AudienceDTO> convertToAudienceDTOList(List<Audience> audienceList) {
        if (CollectionUtils.isEmpty(audienceList)) {
            return null;
        }
        List<AudienceDTO> audienceDTOS = new ArrayList<>();
        audienceList.stream().forEach(audience -> {
            audienceDTOS.add(new AudienceDTO(audience.getAudienceValueId(), audience.getOperator(), null));
        });
        return audienceDTOS;
    }

    public List<Audience> convertFromAudienceDTOList(List<AudienceDTO> audienceDTOList, AudienceType audienceType, Long triggerId) {
        if (CollectionUtils.isEmpty(audienceDTOList)) {
            return null;
        }
        List<Audience> audienceList = new ArrayList<>();
        audienceDTOList.stream().forEach(audienceDTO -> {
            audienceList.add(new Audience(null, triggerId, audienceType, audienceDTO.getId(), audienceDTO.getOperator(), true));
        });
        return audienceList;
    }

    public List<Audience> mergeAudienceList(List<Audience> uiAudienceList, List<Audience> dbAudienceList) {
        if (!CollectionUtils.isEmpty(dbAudienceList)) {
            dbAudienceList.stream().forEach(audience -> {
                audience.setStatus(false);
            });
        }
        if (!CollectionUtils.isEmpty(uiAudienceList)) {
            uiAudienceList.stream().forEach(uiAudience -> {
                Audience dbAudience = dbAudienceList.stream().filter(a -> a.getAudienceValueId().equals(uiAudience.getAudienceValueId())).findFirst().orElse(null);
                if (dbAudience == null) {
                    dbAudienceList.add(uiAudience);
                } else {
                    dbAudience.setStatus(true);
                    dbAudience.setOperator(uiAudience.getOperator());
                }
            });
        }
        return dbAudienceList;
    }
    public VisitBrandPageSettingDTO convertToVisitBrandPageSettingDTO(VisitBrandPageSetting visitBrandPageSetting){
        if(visitBrandPageSetting == null){
            return null;
        }
        VisitBrandPageSettingDTO dto = this.convertToDto(VisitBrandPageSettingDTO.class, visitBrandPageSetting);
        if(!CollectionUtils.isEmpty(visitBrandPageSetting.getVisitBrandPageCollectionRefs())){
            dto.setCollectionIds(visitBrandPageSetting.getVisitBrandPageCollectionRefs().stream().filter(b -> b.getStatus() == true).map(b -> b.getCollectionId()).collect(Collectors.toList()));
        }
        if(!CollectionUtils.isEmpty(visitBrandPageSetting.getVisitBrandPageURLFilterRefs())){
            dto.setUrlVisibleFilters(visitBrandPageSetting.getVisitBrandPageURLFilterRefs().stream().filter(u -> u.getStatus() == true && u.getUrlFilterOperator().equals(Operator.TARGET)).map(u -> u.getUrlCriteria()).collect(Collectors.toList()));
            dto.setUrlIgnoredFilters(visitBrandPageSetting.getVisitBrandPageURLFilterRefs().stream().filter(u -> u.getStatus() == true && u.getUrlFilterOperator().equals(Operator.BLOCK)).map(u -> u.getUrlCriteria()).collect(Collectors.toList()));
        }
        return dto;
    }
    public VisitBrandPageSetting convertFromVisitBrandPageSettingDTO(VisitBrandPageSettingDTO visitBrandPageSettingDTO, Long triggerId){
        VisitBrandPageSetting visitBrandPageSetting = convertFromDto(VisitBrandPageSetting.class, visitBrandPageSettingDTO);
        visitBrandPageSetting.setTriggerId(triggerId);
        if(!CollectionUtils.isEmpty(visitBrandPageSettingDTO.getCollectionIds())){
            visitBrandPageSetting.setVisitBrandPageCollectionRefs(visitBrandPageSettingDTO.getCollectionIds().stream().map(collectionid -> new VisitBrandPageCollectionRef(null, triggerId, collectionid, true, visitBrandPageSetting)).collect(Collectors.toList()));
        }
        List<VisitBrandPageURLFilterRef> brandPageURLVisibleFilterRefList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(visitBrandPageSettingDTO.getUrlVisibleFilters()) ||!CollectionUtils.isEmpty(visitBrandPageSettingDTO.getUrlIgnoredFilters()) ){
            brandPageURLVisibleFilterRefList = visitBrandPageSettingDTO.getUrlVisibleFilters().stream().map(urlFilter -> new VisitBrandPageURLFilterRef(null, triggerId, urlFilter, visitBrandPageSetting, true, Operator.TARGET)).collect(Collectors.toList());


        }
        List<VisitBrandPageURLFilterRef> brandPageURLIgnoredFilterRefList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(visitBrandPageSettingDTO.getUrlIgnoredFilters())){
          brandPageURLIgnoredFilterRefList = visitBrandPageSettingDTO.getUrlIgnoredFilters().stream().map(urlFilter -> new VisitBrandPageURLFilterRef(null, triggerId, urlFilter, visitBrandPageSetting, true, Operator.BLOCK)).collect(Collectors.toList());
        }
        if(!CollectionUtils.isEmpty(brandPageURLVisibleFilterRefList) ||!CollectionUtils.isEmpty(brandPageURLIgnoredFilterRefList) ) {
            visitBrandPageSetting.setVisitBrandPageURLFilterRefs(Stream.of(brandPageURLVisibleFilterRefList, brandPageURLIgnoredFilterRefList).flatMap(x -> x.stream()).collect(Collectors.toList()));
        }
        visitBrandPageSetting.setStatus(true);
        return visitBrandPageSetting;
    }
    public VisitBrandPageSetting mergeVisitBrandPageSettingChanges(VisitBrandPageSetting dbVisitBrandPageSetting, VisitBrandPageSetting uiVisitBrandPageSetting){
        dbVisitBrandPageSetting.setCustomUrlParamValue(uiVisitBrandPageSetting.getCustomUrlParamValue());
        dbVisitBrandPageSetting.setCustomUrlParamName(uiVisitBrandPageSetting.getCustomUrlParamName());
        dbVisitBrandPageSetting.setFrequencyControlValue(uiVisitBrandPageSetting.getFrequencyControlValue());
        dbVisitBrandPageSetting.setUrlFilterOperator(uiVisitBrandPageSetting.getUrlFilterOperator());;
        if(!CollectionUtils.isEmpty(dbVisitBrandPageSetting.getVisitBrandPageCollectionRefs())){
            dbVisitBrandPageSetting.getVisitBrandPageCollectionRefs().forEach(c -> c.setStatus(false));
        } else{
            dbVisitBrandPageSetting.setVisitBrandPageCollectionRefs(new ArrayList<>());
        }
        if(!CollectionUtils.isEmpty(dbVisitBrandPageSetting.getVisitBrandPageURLFilterRefs())){
            dbVisitBrandPageSetting.getVisitBrandPageURLFilterRefs().forEach(u -> u.setStatus(false));
        } else{
            dbVisitBrandPageSetting.setVisitBrandPageURLFilterRefs(new ArrayList<>());
        }
        if(!CollectionUtils.isEmpty(uiVisitBrandPageSetting.getVisitBrandPageCollectionRefs())){
            uiVisitBrandPageSetting.getVisitBrandPageCollectionRefs().stream().forEach(uiCollectionRef -> {
                VisitBrandPageCollectionRef visitBrandPageCollectionRef = dbVisitBrandPageSetting.getVisitBrandPageCollectionRefs().stream().filter(r -> r.getCollectionId().equals(uiCollectionRef.getCollectionId())).findFirst().orElse(null);
                if(visitBrandPageCollectionRef != null){
                    visitBrandPageCollectionRef.setStatus(true);
                } else{
                    uiCollectionRef.setVisitBrandPageSetting(dbVisitBrandPageSetting);
                    dbVisitBrandPageSetting.getVisitBrandPageCollectionRefs().add(uiCollectionRef);
                }
            });
        }
        if(!CollectionUtils.isEmpty(uiVisitBrandPageSetting.getVisitBrandPageURLFilterRefs())){
            uiVisitBrandPageSetting.getVisitBrandPageURLFilterRefs().stream().forEach(uiUrlFilterRef -> {
                VisitBrandPageURLFilterRef visitBrandPageURLFilterRef = dbVisitBrandPageSetting.getVisitBrandPageURLFilterRefs().stream().filter(u -> u.getUrlCriteria().equals(uiUrlFilterRef.getUrlCriteria())).findFirst().orElse(null);
                if(visitBrandPageURLFilterRef != null){
                    visitBrandPageURLFilterRef.setStatus(true);
                } else{
                    uiUrlFilterRef.setVisitBrandPageSetting(dbVisitBrandPageSetting);
                    dbVisitBrandPageSetting.getVisitBrandPageURLFilterRefs().add(uiUrlFilterRef);
                }
            });
        }
        dbVisitBrandPageSetting.setStatus(true);
        return dbVisitBrandPageSetting;
    }
    public ClickSearchAdSettingDTO convertToClickSearchAdSettingDTO(ClickSearchAdSetting clickSearchAdSetting){
        if(clickSearchAdSetting ==null){
            return null;
        }
        ClickSearchAdSettingDTO dto = this.convertToDto(ClickSearchAdSettingDTO.class, clickSearchAdSetting);
        dto.setCustomKeywordQueryOperator(clickSearchAdSetting.getCustomKeywordQueryOperator());
        if(!CollectionUtils.isEmpty(clickSearchAdSetting.getClickSearchAdSettingCollectionRefs())){
            dto.setCollectionIds(clickSearchAdSetting.getClickSearchAdSettingCollectionRefs().stream().filter(c -> c.getStatus() == true).map(c -> c.getCollectionId()).collect(Collectors.toList()));
        }
        if(!CollectionUtils.isEmpty(clickSearchAdSetting.getClickSearchAdSettingKeywordRefs())){
            dto.setKeywords(clickSearchAdSetting.getClickSearchAdSettingKeywordRefs().stream().filter(k -> k.getStatus() == true).map(k -> k.getKeyword()).collect(Collectors.toList()));
        }
        return dto;
    }
    public ClickSearchAdSetting convertFromClickSearchAdSettingDTO(ClickSearchAdSettingDTO clickSearchAdSettingDTO, Long triggerId){
        ClickSearchAdSetting clickSearchAdSetting = convertFromDto(ClickSearchAdSetting.class, clickSearchAdSettingDTO);
        clickSearchAdSetting.setTriggerId(triggerId);
        clickSearchAdSetting.setCustomKeywordQueryOperator(clickSearchAdSettingDTO.getCustomKeywordQueryOperator());
        if(!CollectionUtils.isEmpty(clickSearchAdSettingDTO.getCollectionIds())){
            clickSearchAdSetting.setClickSearchAdSettingCollectionRefs(clickSearchAdSettingDTO.getCollectionIds().stream().map(collectionId -> new ClickSearchAdSettingCollectionRef(null, triggerId, collectionId, true, clickSearchAdSetting)).collect(Collectors.toList()));
        }
        if(!CollectionUtils.isEmpty(clickSearchAdSettingDTO.getKeywords())){
            clickSearchAdSetting.setClickSearchAdSettingKeywordRefs(clickSearchAdSettingDTO.getKeywords().stream().map(keyword -> new ClickSearchAdSettingKeywordRef(null, triggerId, keyword, true, clickSearchAdSetting)).collect(Collectors.toList()));
        }
        clickSearchAdSetting.setStatus(true);
        return clickSearchAdSetting;
    }

    public ClickSearchAdSetting mergeClickSearchAdSetting(ClickSearchAdSetting uiClickSearchAdSetting, ClickSearchAdSetting dbClickSearchAdSetting){
        if(!CollectionUtils.isEmpty(dbClickSearchAdSetting.getClickSearchAdSettingKeywordRefs())){
            dbClickSearchAdSetting.getClickSearchAdSettingKeywordRefs().forEach(k -> k.setStatus(false));
        } else{
            dbClickSearchAdSetting.setClickSearchAdSettingKeywordRefs(new ArrayList<>());
        }
        dbClickSearchAdSetting.setCustomUrlParamName(uiClickSearchAdSetting.getCustomUrlParamName());
        dbClickSearchAdSetting.setCustomUrlParamValue(uiClickSearchAdSetting.getCustomUrlParamValue());
        dbClickSearchAdSetting.setCustomKeywordQueryOperator(uiClickSearchAdSetting.getCustomKeywordQueryOperator());
        if(!CollectionUtils.isEmpty(dbClickSearchAdSetting.getClickSearchAdSettingCollectionRefs())){
            dbClickSearchAdSetting.getClickSearchAdSettingCollectionRefs().forEach(c -> c.setStatus(false));
        } else{
            dbClickSearchAdSetting.setClickSearchAdSettingCollectionRefs(new ArrayList<>());
        }
        if(!CollectionUtils.isEmpty(uiClickSearchAdSetting.getClickSearchAdSettingCollectionRefs())){
            uiClickSearchAdSetting.getClickSearchAdSettingCollectionRefs().stream().forEach(uiCollectionRef -> {
                ClickSearchAdSettingCollectionRef dbClickSearchAdSettingCollectionRef = dbClickSearchAdSetting.getClickSearchAdSettingCollectionRefs().stream().filter(c -> c.getCollectionId().equals(uiCollectionRef.getCollectionId())).findFirst().orElse(null);
                if(dbClickSearchAdSettingCollectionRef != null){
                    dbClickSearchAdSettingCollectionRef.setStatus(true);
                } else{
                    uiCollectionRef.setClickSearchAdSetting(dbClickSearchAdSetting);
                    dbClickSearchAdSetting.getClickSearchAdSettingCollectionRefs().add(uiCollectionRef);
                }
            });
        }
        if(!CollectionUtils.isEmpty(uiClickSearchAdSetting.getClickSearchAdSettingKeywordRefs())){
            uiClickSearchAdSetting.getClickSearchAdSettingKeywordRefs().stream().forEach(uiKeywordRef -> {
                ClickSearchAdSettingKeywordRef dbClickSearchAdSettingKeywordRef = dbClickSearchAdSetting.getClickSearchAdSettingKeywordRefs().stream().filter(k -> k.getKeyword().equals(uiKeywordRef.getKeyword())).findFirst().orElse(null);
                if(dbClickSearchAdSettingKeywordRef != null){
                    dbClickSearchAdSettingKeywordRef.setStatus(true);
                } else{
                    uiKeywordRef.setClickSearchAdSetting(dbClickSearchAdSetting);
                    dbClickSearchAdSetting.getClickSearchAdSettingKeywordRefs().add(uiKeywordRef);
                }
            });
        }
        dbClickSearchAdSetting.setStatus(true);
        return dbClickSearchAdSetting;
    }

    public ExposeMediaSettingDTO convertToExposeMediaSettingDTO(ExposeMediaSetting exposeMediaSetting){
        if(exposeMediaSetting == null){
            return null;
        }
        ExposeMediaSettingDTO exposeMediaSettingDTO = new ExposeMediaSettingDTO();
        exposeMediaSettingDTO.setFrequencyControlValue(exposeMediaSetting.getFrequencyControlValue());
        if(!CollectionUtils.isEmpty(exposeMediaSetting.getExposeMediaSettingCollectionRefs())){
            exposeMediaSettingDTO.setCollectionIds(exposeMediaSetting.getExposeMediaSettingCollectionRefs().stream().filter(c -> c.getStatus() == true).map(c -> c.getCollectionId()).collect(Collectors.toList()));
        }
        return exposeMediaSettingDTO;
    }

    public ExposeMediaSetting convertFromExposeMediaSettingDTO(ExposeMediaSettingDTO exposeMediaSettingDTO, Long triggerId){
        ExposeMediaSetting exposeMediaSetting = new ExposeMediaSetting();
        exposeMediaSetting.setTriggerId(triggerId);
        exposeMediaSetting.setFrequencyControlValue(exposeMediaSettingDTO.getFrequencyControlValue());
        if(!CollectionUtils.isEmpty(exposeMediaSettingDTO.getCollectionIds())){
            exposeMediaSetting.setExposeMediaSettingCollectionRefs(exposeMediaSettingDTO.getCollectionIds().stream().map(collectionId -> new ExposeMediaSettingCollectionRef(null, triggerId, collectionId, true, exposeMediaSetting)).collect(Collectors.toList()));
        }
        exposeMediaSetting.setStatus(true);
        return exposeMediaSetting;
    }

    public ExposeMediaSetting mergeExposeMediaSetting(ExposeMediaSetting uiExposeMediaSetting, ExposeMediaSetting dbExposeMediaSetting){
        dbExposeMediaSetting.setFrequencyControlValue(uiExposeMediaSetting.getFrequencyControlValue());
        if(!CollectionUtils.isEmpty(dbExposeMediaSetting.getExposeMediaSettingCollectionRefs())){
            dbExposeMediaSetting.getExposeMediaSettingCollectionRefs().forEach(c -> c.setStatus(false));
        } else{
            dbExposeMediaSetting.setExposeMediaSettingCollectionRefs(new ArrayList<>());
        }
        if(!CollectionUtils.isEmpty(uiExposeMediaSetting.getExposeMediaSettingCollectionRefs())){
            uiExposeMediaSetting.getExposeMediaSettingCollectionRefs().stream().forEach(uiCollectionRef -> {
                ExposeMediaSettingCollectionRef dbCollectionRef = dbExposeMediaSetting.getExposeMediaSettingCollectionRefs().stream().filter(c -> c.getCollectionId().equals(uiCollectionRef.getCollectionId())).findFirst().orElse(null);
                if(dbCollectionRef != null){
                    dbCollectionRef.setStatus(true);
                } else {
                    uiCollectionRef.setExposeMediaSetting(dbExposeMediaSetting);
                    dbExposeMediaSetting.getExposeMediaSettingCollectionRefs().add(uiCollectionRef);
                }
            });
        }
        dbExposeMediaSetting.setStatus(true);
        return dbExposeMediaSetting;
    }
    public ClickMediaSettingDTO convertToClickMediaSettingDTO(ClickMediaSetting ClickMediaSetting){
        if(ClickMediaSetting == null){
            return null;
        }
        ClickMediaSettingDTO ClickMediaSettingDTO = new ClickMediaSettingDTO();
        ClickMediaSettingDTO.setFrequencyControlValue(ClickMediaSetting.getFrequencyControlValue());
        if(!CollectionUtils.isEmpty(ClickMediaSetting.getClickMediaSettingCollectionRefs())){
            ClickMediaSettingDTO.setCollectionIds(ClickMediaSetting.getClickMediaSettingCollectionRefs().stream().filter(c -> c.getStatus() == true).map(c -> c.getCollectionId()).collect(Collectors.toList()));
        }
        return ClickMediaSettingDTO;
    }

    public ClickMediaSetting convertFromClickMediaSettingDTO(ClickMediaSettingDTO ClickMediaSettingDTO, Long triggerId){
        ClickMediaSetting clickMediaSetting = new ClickMediaSetting();
        clickMediaSetting.setTriggerId(triggerId);
        clickMediaSetting.setFrequencyControlValue(ClickMediaSettingDTO.getFrequencyControlValue());
        if(!CollectionUtils.isEmpty(ClickMediaSettingDTO.getCollectionIds())){
            clickMediaSetting.setClickMediaSettingCollectionRefs(ClickMediaSettingDTO.getCollectionIds().stream().map(collectionId -> new ClickMediaSettingCollectionRef(null, triggerId, collectionId, true, clickMediaSetting)).collect(Collectors.toList()));
        }
        clickMediaSetting.setStatus(true);
        return clickMediaSetting;
    }

    public ClickMediaSetting mergeClickMediaSetting(ClickMediaSetting uiClickMediaSetting, ClickMediaSetting dbClickMediaSetting){
        dbClickMediaSetting.setFrequencyControlValue(uiClickMediaSetting.getFrequencyControlValue());
        if(!CollectionUtils.isEmpty(dbClickMediaSetting.getClickMediaSettingCollectionRefs())){
            dbClickMediaSetting.getClickMediaSettingCollectionRefs().forEach(c -> c.setStatus(false));
        } else{
            dbClickMediaSetting.setClickMediaSettingCollectionRefs(new ArrayList<>());
        }
        if(!CollectionUtils.isEmpty(uiClickMediaSetting.getClickMediaSettingCollectionRefs())){
            uiClickMediaSetting.getClickMediaSettingCollectionRefs().stream().forEach(uiCollectionRef -> {
                ClickMediaSettingCollectionRef dbCollectionRef = dbClickMediaSetting.getClickMediaSettingCollectionRefs().stream().filter(c -> c.getCollectionId().equals(uiCollectionRef.getCollectionId())).findFirst().orElse(null);
                if(dbCollectionRef != null){
                    dbCollectionRef.setStatus(true);
                } else {
                    uiCollectionRef.setClickMediaSetting(dbClickMediaSetting);
                    dbClickMediaSetting.getClickMediaSettingCollectionRefs().add(uiCollectionRef);
                }
            });
        }
        dbClickMediaSetting.setStatus(true);
        return dbClickMediaSetting;
    }
    public List<OpenEmailSettingCollectionRef> mergeOpenEmailSettingCollectionRefs(List<Long> uiRefs, List<OpenEmailSettingCollectionRef> dbRefs, Long triggerId){
        if(!CollectionUtils.isEmpty(dbRefs)){
            dbRefs.forEach(ref -> ref.setStatus(false));
        }
        if(!CollectionUtils.isEmpty(uiRefs)){
            uiRefs.stream().forEach(collectionId -> {
                OpenEmailSettingCollectionRef dbRef= dbRefs.stream().filter(ref -> ref.getCollectionId().equals(collectionId)).findFirst().orElse(null);
                if(dbRef != null){
                    dbRef.setStatus(true);
                } else{
                    dbRefs.add(new OpenEmailSettingCollectionRef(null, triggerId, collectionId, true));
                }
            });
        }
        return dbRefs;
    }
    public List<ClickEmailSettingCollectionRef> mergeClickEmailSettingCollectionRefs(List<Long> uiRefs, List<ClickEmailSettingCollectionRef> dbRefs, Long triggerId){
        if(!CollectionUtils.isEmpty(dbRefs)){
            dbRefs.forEach(ref -> ref.setStatus(false));
        }
        if(!CollectionUtils.isEmpty(uiRefs)){
            uiRefs.stream().forEach(collectionId -> {
                ClickEmailSettingCollectionRef dbRef= dbRefs.stream().filter(ref -> ref.getCollectionId().equals(collectionId)).findFirst().orElse(null);
                if(dbRef != null){
                    dbRef.setStatus(true);
                } else{
                    dbRefs.add(new ClickEmailSettingCollectionRef(null, triggerId, collectionId, true));
                }
            });
        }
        return dbRefs;
    }

    public NPISmartList mergeNPISmartList(NPISmartList uiList, NPISmartList dbList, Long triggerId){
        if(dbList == null){
            dbList = new NPISmartList();
            dbList.setTriggerId(triggerId);
        }
        //dbList.setSmartListName(uiList.getSmartListName());
        dbList.setRemoveAfter(uiList.getRemoveAfter());
        dbList.setHcpRetrospective(uiList.getHcpRetrospective());
        dbList.setGroupId(uiList.getGroupId());
        dbList.setStatus(true);
        return dbList;
    }

    public KeywordSmartList mergeKeywordSmartList(KeywordSmartList uiList, KeywordSmartList dbList, Long triggerId){
        if(dbList == null){
            dbList = new KeywordSmartList();
            dbList.setTriggerId(triggerId);
        }
        //dbList.setSmartListName(uiList.getSmartListName());
        dbList.setRemoveAfter(uiList.getRemoveAfter());
        dbList.setGroupId(uiList.getGroupId());
        dbList.setStatus(true);
        return dbList;
    }
    public Webhook mergeWebhook(Webhook uiWebhook, Webhook dbWebhook){
        //dbWebhook.setNpiMacro(uiWebhook.getNpiMacro());
        dbWebhook.setUrl(uiWebhook.getUrl());
        dbWebhook.setStatus(true);
        dbWebhook.setRequestMethodId(uiWebhook.getRequestMethodId());
        dbWebhook.setContentTypeId(uiWebhook.getContentTypeId());
        dbWebhook.setRequestBody(uiWebhook.getRequestBody());
        return dbWebhook;
    }
}
