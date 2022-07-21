package com.pulsepoint.hcp365.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lookup {
    private Long id;
    private String name;
    private Integer ordinal;

}
