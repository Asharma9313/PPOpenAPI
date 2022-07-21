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
@Table(name = "Hcp365Trigger_Action_Click_Email_CollectionXRef")
@Audited
@AuditTable(value = "AuditAdvHcp365Trigger_Action_Click_Email_CollectionXRef")
@EqualsAndHashCode(callSuper = false)
public class ClickEmailSettingCollectionRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TriggerId")
    private Long triggerId;

    @Column(name="CollectionId")
    private Long collectionId;

    @Column(name="Status")
    private Boolean status;
}
