package com.pulsepoint.hcp365.trigger.advice;

import com.pulsepoint.commons.utils.SecurityContextUtil;
import com.pulsepoint.hcp365.trigger.service.TriggerService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;

@Aspect
@Component
public class TriggerControllerAdvice {

    @Autowired
    TriggerService triggerService;

    @Before("execution(* com.pulsepoint.hcp365.trigger.controller..*(..)) && args(triggerId)")
    public void beforeAdvice(JoinPoint joinPoint, Long triggerId) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        boolean proceed = false;
        if(codeSignature.getParameterNames() != null && codeSignature.getParameterNames().length > 0){
            proceed = Arrays.stream(codeSignature.getParameterNames())
                    .anyMatch(x -> x.equalsIgnoreCase("triggerId"));
        }
        if(proceed && triggerId != null){
            triggerService.validateTriggerbyAccount(triggerId, SecurityContextUtil.getAccountId());
        }
    }
}
