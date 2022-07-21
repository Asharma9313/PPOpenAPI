package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportLogRepository extends JpaRepository<ReportLog, Long>, JpaSpecificationExecutor<ReportLog> {
    ReportLog getByRequestId(String requestId);

    @Query(value = "  select PlacementName, AdvrName, M.AccountName from Hcp365Placement P join adadmin.ADVERTSR A on P.AdvId = A.ADVID \n" +
            "  cross Join MasterAccount M \n" +
            "  where P.Id = :placementId and M.AccountId = :accountId", nativeQuery = true)
    List<Object[]> getReportVariableValues(@Param("placementId") Long placementId, @Param("accountId") Long accountId);
}
