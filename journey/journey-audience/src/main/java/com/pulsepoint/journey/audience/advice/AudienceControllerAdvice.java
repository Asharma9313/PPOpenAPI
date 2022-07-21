package com.pulsepoint.journey.audience.advice;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.commons.utils.SecurityContextUtil;

import com.pulsepoint.journey.audience.modal.AudienceDefinition;
import com.pulsepoint.journey.audience.repository.AudienceDefinitionRepository;
import com.pulsepoint.journey.audience.service.LifeAudienceDefinitionService;
import com.pulsepoint.journey.audience.service.PublisherAudienceDefinitionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.Flow;

@Aspect
@Component
public class AudienceControllerAdvice {
    @Autowired
    LifeAudienceDefinitionService lifeAudienceDefinitionService;
    @Autowired
    PublisherAudienceDefinitionService publisherAudienceDefinitionService;
    @Before("execution(* com.pulsepoint.journey.audience.controller..*(..)) && args(audienceId)")
    public void beforeAdvice(JoinPoint joinPoint, Long audienceId) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        boolean proceed = false;
        if(codeSignature.getParameterNames() != null && codeSignature.getParameterNames().length > 0){
            proceed = Arrays.stream(codeSignature.getParameterNames())
                    .anyMatch(x -> x.equalsIgnoreCase("audienceId"));
        }
        if(proceed && audienceId != null){
            if(codeSignature.getName().toLowerCase().indexOf("publisher") > -1){
                publisherAudienceDefinitionService.validateAudiencebyAccount(audienceId, SecurityContextUtil.getAccountId());
            } else if(codeSignature.getName().toLowerCase().indexOf("life") > -1) {
                lifeAudienceDefinitionService.validateAudiencebyAccount(audienceId, SecurityContextUtil.getAccountId());
            }
        }
    }
}
