package com.pulsepoint.journey.audience.service;

import com.pulsepoint.journey.audience.modal.Scorecard;
import com.pulsepoint.journey.audience.repository.ScorecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScorecardService {
    @Autowired
    private ScorecardRepository scorecardRepository;

    public Scorecard find(Long audienceId){
        return scorecardRepository.findByAudienceId(audienceId);
    }
}
