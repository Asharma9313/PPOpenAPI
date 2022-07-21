package com.pulsepoint.hcp365.modal;

import com.pulsepoint.commons.audit.AuditReport;
import com.pulsepoint.hcp365.enums.ReportPeriodicalType;
import com.pulsepoint.hcp365.enums.ScheduledReportFrequencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ScheduledReport")
@EqualsAndHashCode()
@AuditTable(value = "Hcp365AuditScheduledReport")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Template", identifierProperty = "id")
public class ScheduledReport implements Cloneable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "Name")
    private String name;

    @Column(name = "CustomDestinationId", nullable = false)
    private Long customDestinationId;

    @OneToMany(mappedBy = "scheduledReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Where(clause = "Status = 1")
    private List<ScheduleReportDefinition> scheduleReportDefinitions = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "FrequencyId", nullable = false)
    private ScheduledReportFrequencyType frequencyId;

    @Column(name = "FilePath")
    private String FilePath;

    @Column(name = "FileName", nullable = false)
    private String fileName;

    @Column(name = "AccountId", nullable = false)
    private Long accountId;

    @Column(name = "AdvId", nullable = false)
    private Long advId;

    @Column(name = "Status", nullable = false)
    private boolean status;

    @Column(name="archive")
    private Boolean archive;

    @Column(name = "ScheduleStartDate")
    private Date scheduleStartDate;

    @Column(name = "ScheduleEndDate")
    private Date scheduleEndDate;

    @Column(name = "GenerateReport_WeekDay")
    private int generateReportOnDayId;// For Weekly Frequency values from 1 to 7 (Weekday Id) and for Monthly Frequency values from 1to 30

    @Column(name = "GenerateReport_Time")
    private String generateReportOnTime;

    @Column(name = "PeriodId")
    private int periodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PeriodId", insertable = false, updatable = false)
    private ScheduledReportPeriod period;

    @Column(name = "PeriodicalNumber")
    private Integer periodicalNumber; // eg. values- 2 days or 4 months or1 quarter

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PeriodicalNumberType")
    private ReportPeriodicalType periodicalNumberType;

    @Column(name = "TimezoneId")
    private int timezoneId;

    @Column(name = "UserId")
    private Long userId;

    @Transient
    private List<Lookup> collections;

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
