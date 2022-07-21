package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.modal.BasicPlacement;
import com.pulsepoint.hcp365.repository.BasicPlacementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacementService {

    private static final Logger logger = LoggerFactory.getLogger(PlacementService.class);

    @Autowired
    BasicPlacementRepository placementRepository;

    public List<BasicPlacement> findAll() {
        return placementRepository.findAll();
    }


    public List<BasicPlacement> findByAccountIdAndAdvId(Long accountId, Long advId,Boolean status) {
        return placementRepository.findByAccountIdAndAdvIdAndStatus(accountId, advId,status);
    }
}
