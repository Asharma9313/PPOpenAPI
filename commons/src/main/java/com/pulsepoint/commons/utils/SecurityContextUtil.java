package com.pulsepoint.commons.utils;


import com.pulsepoint.commons.security.UserContextDTO;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {
    public static Long getAccountId(){
        return getUserContext().getAccountId();
    }
    public static boolean isAdmin(){
        return getUserContext().isAdmin();
    }
    public static Long getUserId(){
        return getUserContext().getUserId();
    }
    public static UserContextDTO getUserContext(){
        return (UserContextDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static boolean isSpecialUser(){ return getUserContext().isSpecialUser();}
}
