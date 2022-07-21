package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.ClickEmailSettingCollectionRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickEmailSettingCollectionRefRepository extends JpaRepository<ClickEmailSettingCollectionRef, Long> {
    List<ClickEmailSettingCollectionRef> findByTriggerId(Long triggerId);
}
