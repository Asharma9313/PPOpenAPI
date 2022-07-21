package com.pulsepoint.hcp365.modal;

import com.pulsepoint.hcp365.enums.ReportArchivationType;
import com.pulsepoint.hcp365.enums.ReportFileType;
import com.pulsepoint.commons.audit.AuditReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ReportFormatSetting")
@EqualsAndHashCode(callSuper = false)
@AuditTable(value = "Hcp365AuditReportFormatSetting")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Definition", identifierProperty = "id")
public class ReportFormatSetting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AccountId", nullable = false)
    private Long accountId;

    @Column(name = "UserId", nullable = false)
    private Long userId;

    @Column(name = "AdvId", nullable = false)
    private Long advertiserId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "FileType", nullable = false)
    private ReportFileType fileType;

    @Column(name = "FileCustomExtension")
    private String fileCustomExtension;

    @Column(name = "FileCustomDelimiter")
    private String fileCustomDelimiter;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ArchivationType", nullable = false)
    private ReportArchivationType archivationType;

    @Column(name = "ColumnHeader", nullable = false)
    private boolean columnHeader;
}
