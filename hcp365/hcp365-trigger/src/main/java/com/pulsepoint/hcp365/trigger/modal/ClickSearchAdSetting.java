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
@Table(name = "HCP365Trigger_Action_Click_SearchAd_Setting")
@Audited
@AuditTable(value="AuditAdvHCP365Trigger_Action_Click_SearchAd_Setting")
@EqualsAndHashCode(callSuper = false)
public class ClickSearchAdSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TriggerId")
    private Long triggerId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "CustomKeywordQueryOperator", nullable = true)
    private Operator customKeywordQueryOperator;

    @OneToMany(mappedBy = "clickSearchAdSetting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ClickSearchAdSettingCollectionRef> clickSearchAdSettingCollectionRefs;

    @OneToMany(mappedBy = "clickSearchAdSetting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClickSearchAdSettingKeywordRef> clickSearchAdSettingKeywordRefs;

    @Column(name="Status")
    private Boolean status;

    @Column(name="CustomUrlParamName")
    private String customUrlParamName;

    @Column(name="CustomUrlParamValue")
    private String customUrlParamValue;
}
