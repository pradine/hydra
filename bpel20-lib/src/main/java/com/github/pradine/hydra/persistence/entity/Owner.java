package com.github.pradine.hydra.persistence.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Owner {
    @Id
    private String id;
    
    @Version
    private Long version;
    
    @Column(nullable = false)
    private String activeId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar initialUpdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar lastUpdate;
    
    @Column(nullable = false, updatable = false)
    private Integer majorNumber;
    
    @Column(nullable = false, updatable = false)
    private Integer minorNumber;
    
    protected Owner() {
    }
    
    public Owner(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public Calendar getInitialUpdate() {
        return initialUpdate;
    }

    protected void setInitialUpdate(Calendar initialUpdate) {
        this.initialUpdate = initialUpdate;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    protected void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getMajorNumber() {
        return majorNumber;
    }

    protected void setMajorNumber(Integer majorNumber) {
        this.majorNumber = majorNumber;
    }

    public Integer getMinorNumber() {
        return minorNumber;
    }

    protected void setMinorNumber(Integer minorNumber) {
        this.minorNumber = minorNumber;
    }

    @PrePersist
    protected void prePersist() {
        initialUpdate = Calendar.getInstance();
        lastUpdate = initialUpdate;
        majorNumber = 0;
        minorNumber = 1;
    }

    @PreUpdate
    protected void preUpdate() {
        lastUpdate = Calendar.getInstance();
    }

    @Override
    public String toString() {
        return "Owner [id=" + id + ", version=" + version + ", activeId="
                + activeId + ", initialUpdate=" + initialUpdate
                + ", lastUpdate=" + lastUpdate + ", majorNumber=" + majorNumber
                + ", minorNumber=" + minorNumber + "]";
    }
}
