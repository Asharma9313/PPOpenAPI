package com.pulsepoint.journey.audience.service;

import com.pulsepoint.journey.audience.modal.DSPSpend;
import com.pulsepoint.journey.audience.repository.DSPSpendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DSPSpendService {

    @Autowired
    private DSPSpendRepository repository;

    public List<DSPSpend> findTop5Buyers(Long audienceId){
        return repository.findTop5ByAudienceIdOrderBySpendDesc(audienceId);
    }
}
