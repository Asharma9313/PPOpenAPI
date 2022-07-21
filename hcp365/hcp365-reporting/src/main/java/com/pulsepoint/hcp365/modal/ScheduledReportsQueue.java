package com.pulsepoint.hcp365.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hcp365ScheduledReportsQueue")
@EqualsAndHashCode()
public class ScheduledReportsQueue {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ScheduleId")
    private Long scheduleId;

    @Column(name="ScheduleDate")
    private Date scheduleDate;

    @Column(name="processed")
    private boolean processed;

    @Column(name="ScheduleDateWithOutTimeZone")
    private Date scheduleDateWithOutTimeZone;

}
