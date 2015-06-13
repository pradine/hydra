package com.github.pradine.hydra.persistence.entity;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class InboundDestination {
    @TableGenerator(name = "INBOUND_ID", table = "ID_GENERATOR",
            pkColumnName = "PRIMARY_KEY", valueColumnName = "VALUE",
            pkColumnValue = "inboundId")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "INBOUND_ID")
    private Long id;

    @Version
    private Long version;
    
    private UUID processId;
    
    private String activityId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar arrivalTime;
    
    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "dataId", referencedColumnName = "id")
    private Data data;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public UUID getProcessId() {
        return processId;
    }

    public void setProcessId(UUID processId) {
        this.processId = processId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    protected void setArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
    @PrePersist
    protected void prePersist() {
        if (arrivalTime == null)
            arrivalTime = Calendar.getInstance();
    }

    @Override
    public String toString() {
        return "InboundDestination [id=" + id + ", version=" + version
                + ", processId=" + processId + ", activityId=" + activityId
                + ", arrivalTime=" + arrivalTime + "]";
    }
}
