package com.pulsepoint.hcp365.trigger.modal;

import com.pulsepoint.hcp365.trigger.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365Trigger_Action_Visit_Brand_Setting")
@Audited
@AuditTable(value = "AuditAdvHCP365Trigger_Action_Visit_Brand_Setting")
@EqualsAndHashCode(callSuper = false)
public class VisitBrandPageSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TriggerId")
    private Long triggerId;

    @Column(name="FrequencyControlValue")
    private Integer frequencyControlValue;

    @Column(name="CustomUrlParamName")
    private String customUrlParamName;

    @Column(name="CustomUrlParamValue")
    private String customUrlParamValue;

    @Column(name="Status")
    private Boolean status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "UrlFilterOperator", nullable = true)
    private Operator urlFilterOperator;

    @OneToMany(mappedBy = "visitBrandPageSetting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VisitBrandPageCollectionRef> visitBrandPageCollectionRefs;

    @OneToMany(mappedBy = "visitBrandPageSetting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisitBrandPageURLFilterRef> visitBrandPageURLFilterRefs;

}
