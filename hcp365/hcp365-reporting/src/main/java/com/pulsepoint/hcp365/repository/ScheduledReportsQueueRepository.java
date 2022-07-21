package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.ScheduledReportsQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledReportsQueueRepository extends JpaRepository<ScheduledReportsQueue, Long> {

    @Query(value = "with allRows as\n" +
            "( select  q.scheduleId,  PeriodId, q.id, r.periodicalNumberType, r.periodicalNumber, filePath, fileName, r.AccountId, \n" +
            "r.AdvId, M.AccountName, A.AdvrName,\n" +
            "ROW_NUMBER() OVER (ORDER BY q.scheduleId) rowId\n" +
            "from Hcp365ScheduledReportsQueue q Join Hcp365ScheduledReport r \n" +
            "join MasterAccount M on M.AccountId = r.AccountId\n" +
            "join Adadmin.ADVERTSR A on A.ADVID = r.AdvId\n" +
            "on q.scheduleId = r.id\n" +
            "where processed = 0 and scheduleDate <= getDate() and r.status = 1\n" +
            " ) select * from allRows where rowId <= :count", nativeQuery = true)

    List<Object[]> getScheduledReportsForProcessing(@Param("count") int count);
}
