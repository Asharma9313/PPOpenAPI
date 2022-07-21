package com.pulsepoint.journey.audience.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AuditTable(value = "AuditAdvJourney_Audience_Definition")
@Table(name = "Journey_Audience_Definition")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AudienceType", discriminatorType = DiscriminatorType.INTEGER)
@EqualsAndHashCode(callSuper = false)
@DiscriminatorOptions(force=true)
@Audited
public abstract class AudienceDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Frequency", nullable = true)
    private Integer frequency;

    @Column(name = "Recency", nullable = true)
    private Integer recency;

    @Column(name="Active", nullable = false)
    private boolean active;

    @Column(name="AccountId", nullable = false)
    private Long accountId;

    @Column(name="URLMask")
    private String urlMask;

    @OneToMany(mappedBy = "audienceDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AudienceCondition> audienceConditions;

    @OneToMany(mappedBy = "audienceDefinition", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AudienceRecordingPixelXRef> audienceRecordingPixelXRefs;

    @Column(name = "AudienceType", insertable = false, updatable = false)
    private Long audienceType;

}
