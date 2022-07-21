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
@Table(name = "ADVERTSR", catalog = "adadmin")
public class BasicAdvertiser {
    @Id
    @Column(name="advId", insertable = false, updatable = false)
    Long id;

    @Column(name="advrName", insertable = false, updatable = false)
    String name;
}
