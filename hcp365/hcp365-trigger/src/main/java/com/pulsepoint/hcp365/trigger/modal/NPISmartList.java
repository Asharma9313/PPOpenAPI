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
@Table(name = "HCP365Trigger_Response_NPISmartList")
@Audited
@AuditTable(value = "AuditAdvHCP365Trigger_Response_NPISmartList")
public class NPISmartList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TriggerId")
    private Long triggerId;

    @Column(name="RemoveNPIAfter")
    private Integer removeAfter;

    @Column(name="Status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TriggerId", referencedColumnName = "Id", insertable = false, updatable = false)
    private Trigger trigger;

    @Column(name="NPIGroupId")
    private Long groupId;

    @Column(name="HcpRetrospective")
    private Boolean hcpRetrospective;
}
