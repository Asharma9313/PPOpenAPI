package com.pulsepoint.hcp365.trigger.modal;

import com.pulsepoint.hcp365.trigger.enums.AudienceType;
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
@AuditTable(value = "AuditAdvHCP365Trigger_Audience")
@Table(name = "HCP365Trigger_Audience")
@EqualsAndHashCode(callSuper = false)
@Audited
public class Audience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TriggerId", nullable = false)
    private Long triggerId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "AudienceTypeId", nullable = false)
    private AudienceType audienceType;

    @Column(name = "AudienceValueId", nullable = false)
    private Long audienceValueId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Operator", nullable = false)
    private Operator operator;

    @Column(name = "Status", nullable = false)
    private boolean status;
}
