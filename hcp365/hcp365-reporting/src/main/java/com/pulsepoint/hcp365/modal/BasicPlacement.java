package com.pulsepoint.hcp365.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365Placement")
public class BasicPlacement {
    @Id
    @Column(name="id", insertable = false, updatable = false)
    private Long id;

    @Column(name="placementName", insertable = false, updatable = false)
    private String placementName;

    @Column(name = "AccountId")
    private Long accountId;

    @Column(name = "AdvId")
    private Long advId;


    @Column(name = "Status")
    private Boolean status;

}
