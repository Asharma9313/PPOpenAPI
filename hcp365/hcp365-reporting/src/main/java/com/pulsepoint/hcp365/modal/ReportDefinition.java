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
@Table(name = "Hcp365ReportDefinition")
@EqualsAndHashCode()
@AuditTable(value = "Hcp365AuditReportDefinition")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Definition", identifierProperty = "id")
public class ReportDefinition {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ReportId", referencedColumnName = "reportId")
    private ReportLog reportLog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AttributeTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    private ReportAttributeRef reportAttribute;

    @Column(name = "AttributeTypeId", nullable = false)
    private int reportAttributeTypeId;

    @Column(name = "AttributeValue")
    private String attributeValue;

    @Column(name = "Status")
    private boolean status;
}
