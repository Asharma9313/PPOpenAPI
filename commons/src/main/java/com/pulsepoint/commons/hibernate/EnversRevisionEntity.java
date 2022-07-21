package com.pulsepoint.commons.hibernate;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "AuditRevisionInfo")
@RevisionEntity(EnversRevisionListener.class)

public class EnversRevisionEntity extends DefaultRevisionEntity {

    private Date dt;
    private Long editorId;
    private Long accountId;
    @OneToMany(mappedBy = "revisionId", cascade = {CascadeType.ALL})
    private Set<AuditRevisionCategory> modifiedCategories = new HashSet<>();

    public void addModifiedCategory(String categoryName, String entityName){
        modifiedCategories.add(new AuditRevisionCategory(this.getId(),categoryName,entityName));
    }

    public Set<AuditRevisionCategory> getModifiedCategories() {
        return modifiedCategories;
    }

    public void setModifiedCategories(Set<AuditRevisionCategory> modifiedCategories) {
        this.modifiedCategories = modifiedCategories;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}