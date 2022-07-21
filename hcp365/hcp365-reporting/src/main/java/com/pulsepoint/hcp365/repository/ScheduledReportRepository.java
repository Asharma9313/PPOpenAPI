package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.ScheduledReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledReportRepository extends JpaRepository<ScheduledReport, Long>, JpaSpecificationExecutor<ScheduledReport> {
    ScheduledReport findByAccountIdAndAdvIdAndStatus(Long accountId, Long advId, boolean status);

    @Query(value = "with schedules as (\n" +
            "select Id,\n" +
            "case when FrequencyId = 1 then Convert(date, scheduleStartDate)\n" +
            "when FrequencyId =2 then Convert(date, getdate())\n" +
            "when FrequencyId = 3 then Convert(date, DateAdd(DAY, GenerateReport_WeekDay - DATEPART(dw,scheduleStartDate), dateadd(ww, datediff(ww,scheduleStartDate,getDate()), scheduleStartDate)))\n" +
            "When FrequencyId = 4 then \n" +
            "case when \n" +
            "DATEPART(Day, DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0))) < GenerateReport_WeekDay\n" +
            "then Convert(date, DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0))) else \n" +
            "Convert(date, dateadd(day, GenerateReport_WeekDay - day(getDate()), getDate()))\n" +
            "end\n" +
            "end as ScheduleDate, GenerateReport_Time, TimeZoneId\n" +
            "from Hcp365ScheduledReport\n" +
            "where status =1\n" +
            "and scheduleStartDate <= getDate()\n" +
            "and (scheduleEndDate is null OR convert(date, scheduleEndDate) > = Convert(date, getDate())))\n" +
            "select * from schedules where ScheduleDate = Convert(date, getDate())\n" +
            "and id not in (select ScheduleId from Hcp365ScheduledReportsQueue where Convert(date, ScheduleDateWithOutTimeZone) = Convert(date, getDate()))", nativeQuery = true)
    List<Object[]> getReportsScheduledForToday();
}
