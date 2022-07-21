package com.pulsepoint.hcp365.modal;


import com.pulsepoint.commons.audit.AuditReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365ReportTemplate")
@EqualsAndHashCode(callSuper = false)
@AuditTable(value = "HCP365AuditReportTemplate")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Template", identifierProperty = "id")
public class ReportTemplate extends AbstractTimestampEntity implements Serializable,Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Status", nullable = false)
    private Boolean status;

    @Column(name = "AccountId", nullable = false)
    private Long accountId;

    @Column(name = "UserId", nullable = false)
    private Long userId;

    @Column(name = "AdvId", nullable = false)
    private Long advertiserId;


    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @NotAudited
    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("ordinal")
    @Where(clause = "Status = 1")
    private List<ReportTemplateColumnDefinition> columnDefinitionList;


}
