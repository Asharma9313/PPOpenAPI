package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GeoTargetType")
@EqualsAndHashCode(callSuper = false)
public class GeoTargetType {
    @Id
    @Column(name = "GeoTargetTypeId")
    private Long key;

    @Column(name = "GeoTargetTypeValue")
    private String type;
}
