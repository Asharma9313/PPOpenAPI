package com.pulsepoint.hcp365.modal;


import com.pulsepoint.commons.audit.AuditReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ReportTemplateColumnGroup")
@AuditTable(value = "Hcp365AuditReportTemplateColumnGroup")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Definition", identifierProperty = "id")
@EqualsAndHashCode()
public class ReportTemplateColumnGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "reportTemplateColumnGroup", fetch = FetchType.EAGER)
    @OrderBy("ordinal")
    @Where(clause = "Status = 1")
    private List<ReportTemplateColumn> reportTemplateColumnList;

    @Column(name = "Status", nullable = false)
    private Boolean status;

    @Column(name = "Ordinal", nullable = false)
    private int ordinal;

}
