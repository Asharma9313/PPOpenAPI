package com.pulsepoint.commons.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContextDTO {
    private String userName;
    private Long accountId;
    List<Long> accountIds;
    boolean isAdmin;
    private Long userId;
    private boolean specialUser;
}