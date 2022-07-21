package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCP365Trigger_Response_Webhook")
@Audited
@AuditTable(value = "AuditAdvHCP365Trigger_Response_Webhook")
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TriggerId")
    private Long triggerId;

    @Column(name="Url")
    private String url;

    /*@Column(name="NPIMacro")
    private String npiMacro;*/

    @Column(name="Status")
    private Boolean status;

    @Column(name = "RequestMethodId")
    private Integer requestMethodId;

    @Column(name = "ContentTypeId")
    private Integer contentTypeId;

    @Column(name="RequestBody")
    private String requestBody;
}
