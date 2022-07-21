package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.modal.ScheduledReportsQueue;
import com.pulsepoint.hcp365.repository.ScheduledReportsQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduledReportQueueTransactionalService {
    @Autowired
    ScheduledReportsQueueRepository queueRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ScheduledReportsQueue update(ScheduledReportsQueue scheduledReportsQueue){
        return queueRepository.save(scheduledReportsQueue);
    }
}
