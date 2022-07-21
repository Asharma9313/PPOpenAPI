package com.pulsepoint.journey.audience.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="1")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class LifeAudienceDefinition extends AudienceDefinition {
    @Column(name = "SegmentName", nullable = true)
    private String segmentName; //For Life Users only

    @Column(name = "PixelId", nullable = true)
    private Long pixelId; //For Life Users only
}
