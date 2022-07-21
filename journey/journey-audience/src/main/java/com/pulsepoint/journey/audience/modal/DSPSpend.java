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
@Table(name = "Journey_Audience_DSP_Spend")
public class DSPSpend {
    @Id
    private Long id;
    private Long audienceId;
    private Long dspId;
    private Double spend;
}
