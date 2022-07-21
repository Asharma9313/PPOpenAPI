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
@Table(name = "Hcp365ReportTemplateColumn")
@AuditTable(value = "Hcp365AuditReportTemplateColumn")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Definition", identifierProperty = "id")
@EqualsAndHashCode()
public class ReportTemplateColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Status", nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "GroupId", referencedColumnName = "Id")
    private ReportTemplateColumnGroup reportTemplateColumnGroup;

    @Column(name = "Ordinal", nullable = false)
    private int ordinal;

    @Column(name = "ReportingAPIColName")
    private String reportingAPIColName;
}
