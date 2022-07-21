package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
//This is the new version
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365Trigger")
@AuditTable(value = "AuditAdvHCP365Trigger")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@EqualsAndHashCode(callSuper = false)
public class Trigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "AdvId", nullable = false)
    private Long advId;

    @Column(name = "AccountId", nullable = false)
    private Long accountId;

    @Column(name = "Active", nullable = false)
    private boolean active;

    @Column(name="FromLife")
    private boolean fromLife;

    @Column(name="Comment")
    private String comment;
}
