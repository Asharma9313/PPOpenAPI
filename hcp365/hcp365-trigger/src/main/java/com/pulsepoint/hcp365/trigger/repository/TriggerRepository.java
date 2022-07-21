package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long>, JpaSpecificationExecutor<Trigger> {
    List<Trigger> findByAccountIdAndAdvIdAndActiveAndFromLifeOrderByNameAsc(Long accountId, Long advId, boolean active, boolean fromLife);

    List<Trigger> findByAccountIdAndAdvIdAndActiveAndNameOrderByNameAsc(Long accountId, Long advId, boolean active, String name);
}
