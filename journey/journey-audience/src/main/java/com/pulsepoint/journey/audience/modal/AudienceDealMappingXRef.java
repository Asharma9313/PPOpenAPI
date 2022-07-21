package com.pulsepoint.journey.audience.modal;


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
@AuditTable(value = "AuditAdvJourney_Audience_Deal_Mapping_Xref")
@Table(name = "Journey_Audience_Deal_Mapping_Xref")
@EqualsAndHashCode(callSuper = false)
@Audited
public class AudienceDealMappingXRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AudienceId", referencedColumnName = "id")
    private AudienceDefinition audienceDefinition;

    @Column(name="InternalDealId")
    private Long internalDealId;

    @Column(name = "DSPId")
    private Long dspId;

    @Column(name = "Active")
    private boolean active;
}
