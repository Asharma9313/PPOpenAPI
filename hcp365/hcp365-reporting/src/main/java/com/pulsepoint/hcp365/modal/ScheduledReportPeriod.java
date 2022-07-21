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
@Table(name = "LifeScheduledReportPeriod")
@EqualsAndHashCode()
public class ScheduledReportPeriod {

    @Id
    @Column(name = "PeriodId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long periodId;

    @Column(name = "PeriodName")
    private String periodName;
}
