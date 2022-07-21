package com.pulsepoint.hcp365.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TimeZone")
@EqualsAndHashCode()
public class TimeZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIMEZONEID")
    private Long timeZoneId;

    @Column(name = "TIMEZONENAME")
    private String timeZoneName;

    @Column(name = "TIMEZONELABEL")
    private String timeZoneLabel;

    @Column(name = "TIMEZONECODE")
    private String timeZoneCode;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "DISPLAYONDSP")
    private boolean displayOnDSP;

    @Column(name = "DISPLAYONBUYER")
    private boolean displayOnBuyer;

    @Column(name = "DISPLAYONADMIN")
    private boolean displayOnAdmin;
}
