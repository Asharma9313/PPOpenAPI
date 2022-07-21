package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365Trigger_Action_Media_Click_Setting")
@Audited
@AuditTable(value = "AuditAdvHCP365Trigger_Action_Media_Click_Setting")
@EqualsAndHashCode(callSuper = false)
public class ClickMediaSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TriggerId")
    private Long triggerId;

    @Column(name="FrequencyControlValue")
    private Integer frequencyControlValue;

    @OneToMany(mappedBy = "clickMediaSetting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ClickMediaSettingCollectionRef> clickMediaSettingCollectionRefs;

    @Column(name="Status")
    private Boolean status;
}
