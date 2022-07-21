package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.NPISmartList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NPISmartListRepository extends JpaRepository<NPISmartList, Long> {
    NPISmartList findByTriggerIdAndStatus(Long triggerId, boolean status);
    List<NPISmartList> findByTriggerIdIn(List<Long> triggerIds);
    List<NPISmartList> findByGroupId(Long groupId);
    //List<NPISmartList> findBySmartListNameAndTriggerAccountIdAndStatus(String smartListName, Long accountId, boolean status);
}
