package com.pulsepoint.hcp365.trigger.modal;

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
@Table(name = "HCP365Trigger_Action_Click_SearchAd_KeywordXRef")
@Audited
@AuditTable(value = "AuditAdvHCP365Trigger_Action_Click_SearchAd_KeywordXRef")
@EqualsAndHashCode(callSuper = false)
public class ClickSearchAdSettingKeywordRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TriggerId")

    private Long triggerId;

    @Column(name="Keyword")
    private String keyword;

    @Column(name="Status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "ClickSearchAdSettingId", referencedColumnName = "id")
    private ClickSearchAdSetting clickSearchAdSetting;
}
