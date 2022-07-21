package com.pulsepoint.hcp365.modal;

import com.pulsepoint.commons.audit.AuditReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ScheduleReportAttributeRef")
@EqualsAndHashCode()
@AuditTable(value = "Hcp365AuditScheduleReportAttributeRef")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = " Schedule Report ReportAttribute", identifierProperty = "id")
public class ScheduleReportAttributeRef {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "ScheduleReportAPIColName")
    private String scheduleReportAPIColName;
}
