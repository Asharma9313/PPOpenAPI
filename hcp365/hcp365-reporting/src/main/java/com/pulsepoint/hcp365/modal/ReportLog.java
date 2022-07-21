package com.pulsepoint.hcp365.modal;

import com.pulsepoint.commons.audit.AuditReport;
import com.pulsepoint.hcp365.enums.ReportDownloadType;
import com.pulsepoint.hcp365.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ReportLog")
@AuditTable(value = "HCP365AuditReportLog")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditReport(categories = {AuditReport.Category.REPORTTEMPLATE}, showCreateAsUpdate = true, featureName = "Report Log", identifierProperty = "reportId")
@EqualsAndHashCode()
public class ReportLog extends AbstractTimestampEntity implements Serializable {
    @Id
    @Column(name = "ReportId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "UserId", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "reportLog", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Where(clause = "Status = 1")
    private List<ReportDefinition> reportDefinitions;

    @Column(name = "Query", length = 4000)
    private String query;

    @Column(name = "Status", nullable = false)
    @Type(type = "com.pulsepoint.hcp365.modal.GenericEnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClass", value = "com.pulsepoint.hcp365.enums.ReportStatus"), @org.hibernate.annotations.Parameter(name = "identifierMethod", value = "toInt"), @org.hibernate.annotations.Parameter(name = "valueOfMethod", value = "fromInt")})
    private ReportStatus status;

    @Column(name = "DownloadLink")
    private String downloadLink;

    @Column(name = "Archive")
    private boolean archive;

    @Column(name = "ErrorMessage")
    private String errorMessage;

    @Column(name = "AccountId", nullable = false)
    private Long accountId;

    @Column(name = "RequestId")
    private String requestId;

    @Column(name = "ScheduleId")
    private Long scheduleId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "downloadType", nullable = false)
    private ReportDownloadType downloadType;

    @Column(name = "CustomDestination")
    private Long customDestinationId;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "fileName")
    private String fileName;

    @Transient
    private List<Lookup> collections;


}
