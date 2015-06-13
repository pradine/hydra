package com.github.pradine.hydra.persistence.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Timer {
    @TableGenerator(name = "TIMER_ID", table = "ID_GENERATOR",
            pkColumnName = "PRIMARY_KEY", valueColumnName = "VALUE",
            pkColumnValue = "timerId")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TIMER_ID")
    private Long id;
    
    @Version
    private Long version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;
    
    @Column(nullable = false)
    private String originatorId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar expiryTime;
    
    @Column(nullable = false)
    private Boolean fired;
    
    //Is Long the best representation for a duration?
    @Column(updatable = false)
    private Long repeatDuration;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public ScopeContext getScopeContext() {
        return scopeContext;
    }

    protected void setScopeContext(ScopeContext scopeContext) {
        this.scopeContext = scopeContext;
    }

    public Calendar getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Calendar expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public Boolean isFired() {
        return fired;
    }

    public void setFired(Boolean fired) {
        this.fired = fired;
    }

    public Long getRepeatDuration() {
        return repeatDuration;
    }

    public void setRepeatDuration(Long repeatDuration) {
        this.repeatDuration = repeatDuration;
    }

    @Override
    public String toString() {
        return "Timer [id=" + id + ", version=" + version + ", scopeContext="
                + scopeContext + ", originatorId=" + originatorId
                + ", expiryTime=" + expiryTime + ", fired=" + fired
                + ", repeatDuration=" + repeatDuration + "]";
    }
}
