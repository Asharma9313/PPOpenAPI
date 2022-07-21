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
@AuditTable(value = "AuditAdvJourney_Audience_Recording_Pixel_Xref")
@Table(name = "Journey_Audience_Recording_Pixel_Xref")
@EqualsAndHashCode(callSuper = false)
@Audited
public class AudienceRecordingPixelXRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AudienceId", referencedColumnName = "id")
    private AudienceDefinition audienceDefinition;

    @Column(name="PixelId")
    private Long pixelId;

    @Column(name="Active")
    private Boolean active;
}
