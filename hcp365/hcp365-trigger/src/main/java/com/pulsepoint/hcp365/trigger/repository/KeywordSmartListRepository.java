package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.KeywordSmartList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordSmartListRepository extends JpaRepository<KeywordSmartList, Long> {
    KeywordSmartList findByTriggerId(Long triggerId);
}
