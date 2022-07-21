package com.pulsepoint.journey.audience.repository;

import com.pulsepoint.journey.audience.modal.AudienceDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AudienceDefinitionRepository<T extends AudienceDefinition> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    List<T> findByAccountIdAndActive(Long accountId, boolean active);
    List<T> findByAccountIdAndActiveAndName(Long accountId, boolean active, String name);
}
