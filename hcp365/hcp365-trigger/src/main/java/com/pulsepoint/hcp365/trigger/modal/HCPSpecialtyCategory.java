package com.pulsepoint.hcp365.trigger.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HCPSpecialtyCategory")
@EqualsAndHashCode(callSuper = false)
public class HCPSpecialtyCategory {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GROUPID")
    private Long groupId;
}
