package com.pulsepoint.hcp365.modal;


import com.pulsepoint.commons.audit.AuditReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ScheduleReportDefinition")
@EqualsAndHashCode()
@AuditTable(value = "Hcp365AuditScheduleReportDefinition")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = " Schedule Report Definition", identifierProperty = "id")
public class ScheduleReportDefinition {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ScheduleReportId", referencedColumnName = "Id")
    private ScheduledReport scheduledReport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AttributeTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    private ScheduleReportAttributeRef scheduleReportAttribute;

    @Column(name = "AttributeTypeId", nullable = false)
    private Integer reportAttributeTypeId;

    @Column(name = "AttributeValue")
    private String attributeValue;

    @Column(name = "Status")
    private boolean status;
}

