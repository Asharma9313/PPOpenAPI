package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NPIGroup")
@EqualsAndHashCode(callSuper = false)
public class NPIGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Name")
    private String name;

    @Column(name="Status")
    private boolean status;

    @Column(name="AccountId")
    private Long accountId;
}
