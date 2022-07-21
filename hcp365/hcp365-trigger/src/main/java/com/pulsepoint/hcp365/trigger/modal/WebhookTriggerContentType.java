package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WebhookTriggerContentType {
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "Name", insertable = false, updatable = false)
    private String name;

    @Column(name = "ContentType", insertable = false, updatable = false)
    private String contentType;
}
