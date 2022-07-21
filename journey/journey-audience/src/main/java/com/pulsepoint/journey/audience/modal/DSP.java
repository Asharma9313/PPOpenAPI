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
@Table(name = "Journey_Audience_DSPList")
public class DSP {
    @Id
    private Long accountId;
    private String displayName;
}
