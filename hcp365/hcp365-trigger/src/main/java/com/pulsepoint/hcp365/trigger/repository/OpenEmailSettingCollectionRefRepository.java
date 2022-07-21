package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.OpenEmailSettingCollectionRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenEmailSettingCollectionRefRepository extends JpaRepository<OpenEmailSettingCollectionRef, Long> {
    List<OpenEmailSettingCollectionRef> findByTriggerId(Long triggerId);
}
