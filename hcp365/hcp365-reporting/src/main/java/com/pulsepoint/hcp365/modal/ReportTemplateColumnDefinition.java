package com.pulsepoint.hcp365.modal;

import com.pulsepoint.hcp365.enums.ReportTemplateFieldType;
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
@Table(name = "Hcp365ReportTemplateColumnDefinition")
@AuditTable(value = "Hcp365AuditReportTemplateColumnDefinition")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Definition", identifierProperty = "id")
@EqualsAndHashCode()
public class ReportTemplateColumnDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TemplateId", referencedColumnName = "Id", nullable = false)
    private ReportTemplate template;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "FieldType", nullable = false)
    private ReportTemplateFieldType fieldType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FieldRefId", insertable = false, updatable = false)
    private ReportTemplateColumn templateColumn;

    @Column(name = "FieldRefId")
    private Long fieldRefId;

    @Column(name = "CustomFieldName")
    private String customFieldName;

    @Column(name = "Ordinal", nullable = false)
    private int ordinal;

    @Column(name = "Status")
    private boolean status;
}
