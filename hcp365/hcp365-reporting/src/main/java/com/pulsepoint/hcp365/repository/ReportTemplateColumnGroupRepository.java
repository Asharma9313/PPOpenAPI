package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.ReportTemplateColumnGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTemplateColumnGroupRepository extends JpaRepository<ReportTemplateColumnGroup, Long> {
    List<ReportTemplateColumnGroup> findByStatusOrderByOrdinal(boolean status);
}
