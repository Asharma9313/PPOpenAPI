package com.pulsepoint.hcp365.trigger.service;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.hcp365.trigger.modal.Trigger;
import com.pulsepoint.hcp365.trigger.repository.TriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriggerService {

    @Autowired
    TriggerRepository triggerRepository;

    public Trigger save(Trigger trigger) {
        checkForDuplicates(trigger);
        return triggerRepository.save(trigger);
    }

    private void checkForDuplicates(Trigger trigger) {
        List<Trigger> existingTriggers = triggerRepository.findByAccountIdAndAdvIdAndActiveAndNameOrderByNameAsc(trigger.getAccountId(), trigger.getAdvId(), true, trigger.getName());
        if (existingTriggers.size() > 0) {
            if (existingTriggers.size() == 1 && trigger.getId() != null && trigger.getId().equals(existingTriggers.get(0).getId())) {
                return;
            }
            throw new InvalidDataException("Smart Action name already exists");
        }
    }

    public void validateTriggerbyAccount(Long triggerId, Long accountId) {
        Trigger trigger = triggerRepository.getById(triggerId);
        if (trigger.getAccountId().equals(accountId) == false) {
            throw new InvalidDataException(" Invalid data");
        }
    }
    public void validateTriggersbyAccount(List<Trigger> triggers, Long accountId) {
        triggers.stream().forEach(trigger -> {
            if (trigger.getId() != null && trigger.getId() > 0 && trigger.getAccountId().equals(accountId) == false) {
                throw new InvalidDataException(" Invalid data");
            }
        });

    }
    public List<Trigger> findByAccountIdAndAdvId(Long accountId, Long advId, boolean fromLife) {
        return triggerRepository.findByAccountIdAndAdvIdAndActiveAndFromLifeOrderByNameAsc(accountId, advId, true, fromLife);
    }

    public Trigger findById(Long id) {
        return triggerRepository.findById(id).get();
    }

    public List<Trigger> findAllById(List<Long> ids){
        return triggerRepository.findAllById(ids);
    }

}
