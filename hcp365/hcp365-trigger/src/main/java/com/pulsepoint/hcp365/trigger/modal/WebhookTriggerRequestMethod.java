package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WebhookTriggerRequestMethod {
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "name", insertable = false, updatable = false)
    private String name;
}
