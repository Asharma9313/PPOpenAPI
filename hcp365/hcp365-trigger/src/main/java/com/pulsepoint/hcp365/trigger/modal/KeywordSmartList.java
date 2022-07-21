package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365Trigger_Response_KeywordSmartList")
@Audited
@AuditTable(value = "AuditAdvHCP365Trigger_Response_KeywordSmartList")
public class KeywordSmartList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TriggerId")
    private Long triggerId;

    @Column(name="RemoveKeywordAfter")
    private Integer removeAfter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TriggerId", referencedColumnName = "Id", insertable = false, updatable = false)
    private Trigger trigger;

    @Column(name="Status")
    private Boolean status;

    @Column(name="KeywordGroupId")
    private Long groupId;
}
