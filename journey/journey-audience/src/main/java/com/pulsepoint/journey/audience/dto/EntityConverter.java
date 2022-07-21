package com.pulsepoint.journey.audience.dto;

import com.pulsepoint.journey.audience.modal.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public LifeAudienceDefinitionDTO convertToLifeAudienceDefinitionDTO(AudienceDefinition audienceDefinition){
        if(audienceDefinition == null){
            return null;
        }
        LifeAudienceDefinitionDTO dto = convertToDto(LifeAudienceDefinitionDTO.class, audienceDefinition);
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceConditions()) == false){
            dto.setAudienceConditionDTOList(audienceDefinition.getAudienceConditions().stream().filter(audienceCondition -> audienceCondition.isActive() == true).map(audienceCondition -> convertToDto(AudienceConditionDTO.class, audienceCondition)).collect(Collectors.toList()));
        }
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceRecordingPixelXRefs()) == false){
            dto.setRecordingPixelIds(audienceDefinition.getAudienceRecordingPixelXRefs().stream().filter(audienceRecordingPixelXRef -> audienceRecordingPixelXRef.getActive() == true).map(audienceRecordingPixelXRef -> audienceRecordingPixelXRef.getPixelId()).collect(Collectors.toList()));
        }
        return dto;
    }

    public LifeAudienceDefinition convertFromDTO(AudienceDefinitionDTO audienceDefinitionDTO){
        if(audienceDefinitionDTO == null){
            return null;
        }
        LifeAudienceDefinition audienceDefinition = convertToDto(LifeAudienceDefinition.class, audienceDefinitionDTO);
        /*if(CollectionUtils.isEmpty(audienceDefinitionDTO.getAudienceConditionDTOList()) == false){
            audienceDefinition.setAudienceConditions(new ArrayList<>());
            audienceDefinitionDTO.getAudienceConditionDTOList().stream().forEach(audienceConditionDTO -> {
                audienceDefinition.getAudienceConditions().add(convertFromDto(AudienceCondition.class, audienceConditionDTO));
            });
            audienceDefinition.getAudienceConditions().stream().forEach(audienceCondition -> {
                audienceCondition.setActive(audienceDefinition.isActive());
                audienceCondition.setAudienceDefinition(audienceDefinition);
            });
        }*/
        this.convertAudienceConditionsFromDTO(audienceDefinitionDTO.getAudienceConditionDTOList(), audienceDefinition);
        this.convertAudienceRecordingPixelXRefs(audienceDefinitionDTO.getRecordingPixelIds(), audienceDefinition);
        /*audienceDefinition.setAudienceRecordingPixelXRefs(new ArrayList<>());
        audienceDefinitionDTO.getRecordingPixelIds().stream().forEach(recordingPixelId -> {
            AudienceRecordingPixelXRef recordingPixelXRef = new AudienceRecordingPixelXRef();
            recordingPixelXRef.setActive(audienceDefinition.isActive());
            recordingPixelXRef.setAudienceDefinition(audienceDefinition);
            recordingPixelXRef.setPixelId(recordingPixelId);
            audienceDefinition.getAudienceRecordingPixelXRefs().add(recordingPixelXRef);
        });*/
        return audienceDefinition;
    }
    private void convertAudienceRecordingPixelXRefs(List<Long> recordingPixelIds, AudienceDefinition audienceDefinition){
        audienceDefinition.setAudienceRecordingPixelXRefs(new ArrayList<>());
        recordingPixelIds.stream().forEach(recordingPixelId -> {
            AudienceRecordingPixelXRef recordingPixelXRef = new AudienceRecordingPixelXRef();
            recordingPixelXRef.setActive(audienceDefinition.isActive());
            recordingPixelXRef.setAudienceDefinition(audienceDefinition);
            recordingPixelXRef.setPixelId(recordingPixelId);
            audienceDefinition.getAudienceRecordingPixelXRefs().add(recordingPixelXRef);
        });
    }
    private void convertAudienceConditionsFromDTO(List<AudienceConditionDTO> audienceConditionDTOS, AudienceDefinition audienceDefinition){
        if(CollectionUtils.isEmpty(audienceConditionDTOS) == false){
            audienceDefinition.setAudienceConditions(new ArrayList<>());
            audienceConditionDTOS.stream().forEach(audienceConditionDTO -> {
                audienceDefinition.getAudienceConditions().add(convertFromDto(AudienceCondition.class, audienceConditionDTO));
            });
            audienceDefinition.getAudienceConditions().stream().forEach(audienceCondition -> {
                audienceCondition.setActive(audienceDefinition.isActive());
                audienceCondition.setAudienceDefinition(audienceDefinition);
            });
        }

    }
    private AudienceDefinition mergeAudienceDefinition(AudienceDefinition audienceDefinitionDB, AudienceDefinition audienceDefinitionUI){
        audienceDefinitionDB.setActive(audienceDefinitionUI.isActive());
        audienceDefinitionDB.setFrequency(audienceDefinitionUI.getFrequency());
        audienceDefinitionDB.setName(audienceDefinitionUI.getName());
        audienceDefinitionDB.setUrlMask(audienceDefinitionUI.getUrlMask());
        audienceDefinitionDB.setRecency(audienceDefinitionUI.getRecency());

        if(CollectionUtils.isEmpty(audienceDefinitionDB.getAudienceConditions()) == false){
            audienceDefinitionDB.getAudienceConditions().stream().forEach(audienceCondition -> {
                audienceCondition.setActive(false);
            });
        } else{
            audienceDefinitionDB.setAudienceConditions(new ArrayList<>());
        }
        if(CollectionUtils.isEmpty(audienceDefinitionUI.getAudienceConditions()) == false) {
            audienceDefinitionUI.getAudienceConditions().stream().forEach(audienceConditionUI -> {
                if(audienceConditionUI.getId() != null && audienceConditionUI.getId() > 0){
                    AudienceCondition audienceConditionDb = audienceDefinitionDB.getAudienceConditions().stream().filter(audienceConditionDB -> audienceConditionDB.getId().equals(audienceConditionUI.getId())).findFirst().get();
                    audienceConditionDb.setActive(true);
                    audienceConditionDb.setConditionType(audienceConditionUI.getConditionType());
                    audienceConditionDb.setConditionValue(audienceConditionUI.getConditionValue());
                } else{
                    audienceDefinitionDB.getAudienceConditions().add(audienceConditionUI);
                    audienceConditionUI.setAudienceDefinition(audienceDefinitionDB);
                    audienceConditionUI.setActive(true);
                }
            });
        }
        audienceDefinitionDB.getAudienceRecordingPixelXRefs().stream().forEach(audienceRecordingPixelXRef -> audienceRecordingPixelXRef.setActive(false));
        audienceDefinitionUI.getAudienceRecordingPixelXRefs().stream().forEach(audienceRecordingPixelXRefUI -> {
            AudienceRecordingPixelXRef audienceRecordingPixelXRef = audienceDefinitionDB.getAudienceRecordingPixelXRefs().stream().filter(p -> p.getPixelId().equals(audienceRecordingPixelXRefUI.getPixelId())).findFirst().orElse(null);
            if (audienceRecordingPixelXRef == null) {
                audienceDefinitionDB.getAudienceRecordingPixelXRefs().add(audienceRecordingPixelXRefUI);
                audienceRecordingPixelXRefUI.setAudienceDefinition(audienceDefinitionDB);
            } else {
                audienceRecordingPixelXRef.setActive(true);
            }

        });
        return audienceDefinitionDB;
    }
    public LifeAudienceDefinition mergeLifeAudienceDefinition(LifeAudienceDefinition audienceDefinitionDB, LifeAudienceDefinition audienceDefinitionUI){
        audienceDefinitionDB = (LifeAudienceDefinition) this.mergeAudienceDefinition(audienceDefinitionDB, audienceDefinitionUI);
        audienceDefinitionDB.setSegmentName(audienceDefinitionUI.getSegmentName());
        audienceDefinitionDB.setPixelId(audienceDefinitionUI.getPixelId());
        return audienceDefinitionDB;
    }
    public PublisherAudienceDefinition mergePublisherAudienceDefinition(PublisherAudienceDefinition audienceDefinitionDB, PublisherAudienceDefinition audienceDefinitionUI){
        audienceDefinitionDB = (PublisherAudienceDefinition) this.mergeAudienceDefinition(audienceDefinitionDB, audienceDefinitionUI);
        audienceDefinitionDB.setDealName(audienceDefinitionUI.getDealName());
        audienceDefinitionDB.setDealDescription(audienceDefinitionUI.getDealDescription());
        audienceDefinitionDB.setDealPrice(audienceDefinitionUI.getDealPrice());
        if(CollectionUtils.isEmpty(audienceDefinitionDB.getAudienceDealMappingXRefs()) == false){
            audienceDefinitionDB.getAudienceDealMappingXRefs().stream().forEach(audienceDealMappingXRef -> audienceDealMappingXRef.setActive(false));
        } else{
            audienceDefinitionDB.setAudienceDealMappingXRefs(new ArrayList<>());
        }
        if(CollectionUtils.isEmpty(audienceDefinitionUI.getAudienceDealMappingXRefs()) == false){
            for(AudienceDealMappingXRef audienceDealMappingXRefUI: audienceDefinitionUI.getAudienceDealMappingXRefs()){
                AudienceDealMappingXRef audienceDealMappingXRef = audienceDefinitionDB.getAudienceDealMappingXRefs().stream().filter(audienceDealMappingXRefDB -> audienceDealMappingXRefDB.getDspId().equals(audienceDealMappingXRefUI.getDspId())).findFirst().orElse(null);
                if(audienceDealMappingXRef == null){
                    audienceDefinitionDB.getAudienceDealMappingXRefs().add(audienceDealMappingXRefUI);
                    audienceDealMappingXRefUI.setActive(true);
                    audienceDealMappingXRefUI.setAudienceDefinition(audienceDefinitionDB);
                } else{
                    audienceDealMappingXRef.setActive(true);;
                }
            }
        }
        return audienceDefinitionDB;
    }
    public PublisherAudienceDefinitionDTO convertToPublisherAudienceDefinitionDTO(PublisherAudienceDefinition audienceDefinition){
        if(audienceDefinition == null){
            return null;
        }
        PublisherAudienceDefinitionDTO dto = convertToDto(PublisherAudienceDefinitionDTO.class, audienceDefinition);
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceConditions()) == false){
            dto.setAudienceConditionDTOList(audienceDefinition.getAudienceConditions().stream().filter(audienceCondition -> audienceCondition.isActive() == true).map(audienceCondition -> convertToDto(AudienceConditionDTO.class, audienceCondition)).collect(Collectors.toList()));
        }
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceRecordingPixelXRefs()) == false){
            dto.setRecordingPixelIds(audienceDefinition.getAudienceRecordingPixelXRefs().stream().filter(audienceRecordingPixelXRef -> audienceRecordingPixelXRef.getActive() == true).map(audienceRecordingPixelXRef -> audienceRecordingPixelXRef.getPixelId()).collect(Collectors.toList()));
        }
        if(CollectionUtils.isEmpty(audienceDefinition.getAudienceDealMappingXRefs()) == false){
            dto.setDealMappingXRefDTOS(audienceDefinition.getAudienceDealMappingXRefs().stream().filter(audienceDealMappingXRef -> audienceDealMappingXRef.isActive() == true).map(audienceDealMappingXRef -> convertToDto(PublisherAudienceDealMappingXRefDTO.class, audienceDealMappingXRef)).collect(Collectors.toList()));
        }
        return dto;
    }
    public PublisherAudienceDefinition convertFromDTO(PublisherAudienceDefinitionDTO audienceDefinitionDTO){
        if(audienceDefinitionDTO == null){
            return null;
        }
        PublisherAudienceDefinition audienceDefinition = convertToDto(PublisherAudienceDefinition.class, audienceDefinitionDTO);
        this.convertAudienceConditionsFromDTO(audienceDefinitionDTO.getAudienceConditionDTOList(), audienceDefinition);
        this.convertAudienceRecordingPixelXRefs(audienceDefinitionDTO.getRecordingPixelIds(), audienceDefinition);
        if(CollectionUtils.isEmpty(audienceDefinitionDTO.getDealMappingXRefDTOS()) == false){
            audienceDefinition.setAudienceDealMappingXRefs(convertToDtoList(AudienceDealMappingXRef.class, audienceDefinitionDTO.getDealMappingXRefDTOS()));
            audienceDefinition.getAudienceDealMappingXRefs().stream().forEach(audienceDealMappingXRef -> {
                audienceDealMappingXRef.setAudienceDefinition(audienceDefinition);
            });
        }
        return audienceDefinition;
    }
}
