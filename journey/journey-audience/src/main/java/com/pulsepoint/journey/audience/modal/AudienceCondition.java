package com.pulsepoint.journey.audience.modal;

import com.pulsepoint.journey.audience.enums.AudienceConditionType;
import com.pulsepoint.journey.audience.enums.Operator;
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
@AuditTable(value = "AuditAdvJourney_Audience_Conditions")
@Table(name = "Journey_Audience_Conditions")
@EqualsAndHashCode(callSuper = false)
@Audited
public class AudienceCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AudienceId", referencedColumnName = "id")
    private AudienceDefinition audienceDefinition;

    @Column(name="ConditionTypeId")
    private AudienceConditionType conditionType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Operator", nullable = true)
    private Operator operator;

    @Column(name="ConditionValue")
    private String conditionValue;

    @Column(name="Active")
    private boolean active;

}
