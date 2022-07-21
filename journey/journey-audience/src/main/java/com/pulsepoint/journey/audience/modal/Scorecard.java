package com.pulsepoint.journey.audience.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Journey_Audience_Scorecard")
public class Scorecard {
    @Id
    private Long id;
    private Long audienceId;
    private double revenue;
    private Long pageViews;
    private Long devices;
    private Long avgViewsPerDay;
    private Long uniquePages;
}
