package com.pulsepoint.hcp365.modal;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@MappedSuperclass
public abstract class AbstractTimestampEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedOn", nullable = false)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastModifiedDate", nullable = false)
    private Date lastModified;

    @PrePersist
    protected void onCreate() {
        lastModified = createdOn = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }
}