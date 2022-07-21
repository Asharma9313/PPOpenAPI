package com.pulsepoint.journey.audience.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(value="2")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class PublisherAudienceDefinition extends AudienceDefinition{
    @Column(name="DealName")
    private String dealName;

    @Column(name="DealDescription")
    private String dealDescription;

    @Column(name="DealPrice")
    private Double dealPrice;

    @OneToMany(mappedBy = "audienceDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AudienceDealMappingXRef> audienceDealMappingXRefs;
}
