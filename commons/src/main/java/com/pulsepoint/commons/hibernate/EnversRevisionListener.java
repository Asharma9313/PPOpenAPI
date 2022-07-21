package com.pulsepoint.commons.hibernate;


import com.pulsepoint.commons.audit.AuditReport;
import com.pulsepoint.commons.security.UserContextDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EnversRevisionListener implements EntityTrackingRevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Optional<UserContextDTO> principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserContextDTO.class::cast);
        EnversRevisionEntity audit = (EnversRevisionEntity) revisionEntity;
        UserContextDTO userContextDTO = principal.orElse(new UserContextDTO());
        audit.setAccountId(userContextDTO.getAccountId());
        audit.setEditorId(userContextDTO.getUserId());
        audit.setDt(new Timestamp(System.currentTimeMillis()));

    }

    @Override
    public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType, Object revisionEntity) {
        AuditReport auditReportAnnotation = (AuditReport) entityClass.getAnnotation(AuditReport.class);
        if (Objects.nonNull(auditReportAnnotation)) {
            AuditReport.Category[] auditReportCategories = auditReportAnnotation.categories();
            if (ArrayUtils.isNotEmpty(auditReportCategories)) {
                Arrays.stream(auditReportCategories).
                        filter(auditReportCategory -> Objects.nonNull(auditReportCategory) && !entityAlreadyExists((EnversRevisionEntity) revisionEntity, auditReportCategory, entityClass.getCanonicalName()))
                        .forEach(auditReportCategory -> ((EnversRevisionEntity) revisionEntity)
                                .addModifiedCategory(auditReportCategory.toString(), entityClass.getCanonicalName()));
            }
        }
    }

    private boolean entityAlreadyExists(EnversRevisionEntity revisionEntity, AuditReport.Category auditReportCategory, String auditedClassName) {
        Set<AuditRevisionCategory> modifiedCategories = revisionEntity.getModifiedCategories();
        return modifiedCategories
                .stream()
                .anyMatch(eachCategory -> eachCategory.getEntityName().equalsIgnoreCase(auditedClassName) && auditReportCategory.toString().equalsIgnoreCase(eachCategory.getCategoryName()));
    }
}

