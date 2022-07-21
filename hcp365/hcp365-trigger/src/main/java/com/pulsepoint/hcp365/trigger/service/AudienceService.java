package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.hcp365.trigger.enums.AudienceType;
import com.pulsepoint.hcp365.trigger.modal.Audience;
import com.pulsepoint.hcp365.trigger.modal.HCPSpecialtyCategory;
import com.pulsepoint.hcp365.trigger.modal.NPIGroup;
import com.pulsepoint.hcp365.trigger.repository.AudienceRepository;
import com.pulsepoint.hcp365.trigger.repository.HCPSpecialtyCategoryRepository;
import com.pulsepoint.hcp365.trigger.repository.NPIGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AudienceService {

    @Autowired
    AudienceRepository audienceRepository;

    @Autowired
    NPIGroupRepository npiGroupRepository;

    @Autowired
    HCPSpecialtyCategoryRepository hcpSpecialtyCategoryRepository;

    public List<Audience> findActiveAudience(Long triggerId, AudienceType audienceType) {
        return audienceRepository.findByTriggerIdAndAudienceTypeAndStatus(triggerId, audienceType, true);
    }

    public List<Audience> findAllAudience(Long triggerId, AudienceType audienceType) {
        return audienceRepository.findByTriggerIdAndAudienceType(triggerId, audienceType);
    }

    public List<Audience> save(List<Audience> audienceList) {
        audienceList.stream().forEach(audience -> {
            audienceRepository.save(audience);
        });
        return audienceList;
    }

    public List<NPIGroup> findNPIGroupsByIds(List<Long> ids, Long accountId){
        return npiGroupRepository.findByAccountIdAndIds(accountId, ids);
    }

    public void validateNPIList(List<Long> npiGroupIds, Long accountId){
        if(findNPIGroupsByIds(npiGroupIds, accountId).size() != npiGroupIds.size()){
            throw new InvalidDataException("Invalid data");
        }
    }

    public List<HCPSpecialtyCategory> findHCPSpecalitiesByIds(List<Long> ids){
        return hcpSpecialtyCategoryRepository.findByGroupIdIn(ids);
    }

    public List<Audience> findActiveByByAudienceTypeAndAndAudienceValue(AudienceType audienceType, Long valudId){
        return audienceRepository.findByAudienceTypeAndStatusAndAudienceValueId(audienceType, true, valudId);
    }

}
