package com.github.pradine.hydra.persistence.entity;

/*
 * #%L
 * bpel20-lib
 * %%
 * Copyright (C) 2015 the original author or authors.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@IdClass(ActivityState.ActivityStatePK.class)
public class ActivityState {
    public static enum State { INITIAL, READY, SKIPPING, SKIPPED, DPE, ACTIVE, COMPLETING, COMPLETED, FAULTING, FAULTED, INTERRUPTING, INTERRUPTED, EXITING, EXITED }
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;

    @Id
    private String activityId;
    
    @Id
    private Integer counter;
    
    @Version
    private Long version;
    
    private String activityName;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar initialUpdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar lastUpdate;
    
    @Enumerated(EnumType.STRING)
    private State state;
    
    protected ActivityState() {
    }
    
    public ActivityState(String activityId) {
        this(activityId, 0);
    }

    public ActivityState(String activityId, Integer counter) {
        this.activityId = activityId;
        this.counter = counter;
    }

    public String getActivityId() {
        return activityId;
    }

    public Integer getCounter() {
        return counter;
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @PrePersist
    protected void prePersist() {
        initialUpdate = Calendar.getInstance();
        lastUpdate = initialUpdate;
    }

    @PreUpdate
    protected void preUpdate() {
        lastUpdate = Calendar.getInstance();
    }
    
    @Override
    public String toString() {
        return "ActivityState [scopeContext=" + scopeContext + ", activityId="
                + activityId + ", counter=" + counter + ", version=" + version
                + ", activityName=" + activityName + ", initialUpdate=" + initialUpdate
                + ", lastUpdate=" + lastUpdate + ", state=" + state + "]";
    }

    public static class ActivityStatePK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 7496835822025183951L;

        private Long scopeContext;

        private String activityId;
        
        private Integer counter;

        public ActivityStatePK() {
        }

        public ActivityStatePK(Long scopeContext, String activityId, Integer counter) {
            this.scopeContext = scopeContext;
            this.activityId = activityId;
            this.counter = counter;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((activityId == null) ? 0 : activityId.hashCode());
            result = prime * result
                    + ((counter == null) ? 0 : counter.hashCode());
            result = prime * result
                    + ((scopeContext == null) ? 0 : scopeContext.hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ActivityStatePK other = (ActivityStatePK) obj;
            if (activityId == null) {
                if (other.activityId != null)
                    return false;
            } else if (!activityId.equals(other.activityId))
                return false;
            if (counter == null) {
                if (other.counter != null)
                    return false;
            } else if (!counter.equals(other.counter))
                return false;
            if (scopeContext == null) {
                if (other.scopeContext != null)
                    return false;
            } else if (!scopeContext.equals(other.scopeContext))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ActivityStatePK [scopeContext=" + scopeContext
                    + ", activityId=" + activityId + ", counter=" + counter
                    + "]";
        }
    }
}
