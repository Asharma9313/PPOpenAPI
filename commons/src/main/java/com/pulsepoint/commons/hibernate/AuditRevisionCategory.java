package com.pulsepoint.commons.hibernate;


import com.pulsepoint.commons.audit.MappedClass;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "AuditRevisionCategory")
public class AuditRevisionCategory implements MappedClass, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "revision")
    private Integer revisionId;

    @Column(name = "entityName")
    private String entityName;

    public AuditRevisionCategory() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revision", insertable = false, updatable = false)
    private EnversRevisionEntity revisionEntity;

    public AuditRevisionCategory(Integer revisionId, String categoryName, String entityName) {
        this.categoryName = categoryName;
        this.revisionId = revisionId;
        this.entityName = entityName;
    }

    @Override
    public Long getId() {
        return new Long(id);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public EnversRevisionEntity getRevisionEntity() {
        return revisionEntity;
    }

    public void setRevisionEntity(EnversRevisionEntity revisionEntity) {
        this.revisionEntity = revisionEntity;
    }

    public Integer getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Integer revisionId) {
        this.revisionId = revisionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}