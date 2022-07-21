package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long>,JpaSpecificationExecutor<ReportTemplate> {
    ReportTemplate findByAccountIdAndAdvertiserIdAndStatus(Long accountId, Long advertiserId, Boolean status);
}
