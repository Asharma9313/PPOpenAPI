package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.hcp365.trigger.modal.NPIGroup;
import com.pulsepoint.hcp365.trigger.repository.NPIGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NPIGroupService {
    @Autowired
    NPIGroupRepository npiGroupRepository;

    public List<NPIGroup> findNPIGroupsByIds(List<Long> ids, Long accountId){
        return npiGroupRepository.findByAccountIdAndIds(accountId, ids);
    }
}
