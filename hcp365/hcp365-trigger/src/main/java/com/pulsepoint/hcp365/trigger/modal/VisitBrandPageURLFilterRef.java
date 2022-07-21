package com.pulsepoint.hcp365.trigger.modal;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365Trigger_Action_Visit_Brand_URL_Filters")
@Audited
@AuditTable(value="AuditAdvHCP365Trigger_Action_Visit_Brand_URL_Filters")
@EqualsAndHashCode(callSuper = false)
public class VisitBrandPageURLFilterRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TriggerId")
    private Long triggerId;

    @Column(name="URLCriteria")
    private String urlCriteria;

    @ManyToOne
    @JoinColumn(name = "VisitBrandSettingId", referencedColumnName = "id")
    private VisitBrandPageSetting visitBrandPageSetting;

    @Column(name="Status")
    private Boolean status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "UrlFilterOperator", nullable = true)
    private Operator urlFilterOperator;


}
