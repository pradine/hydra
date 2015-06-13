package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

//TODO Consider that any subclasses, that represent actual correlations, should be cleaned
//up whenever the corresponding correlation set goes out of scope.
@MappedSuperclass
public abstract class AbstractCorrelation {
    @Version
    protected Long version;
    
    @Column(updatable = false, nullable = false, length = 36)
    protected UUID processId;
    
    protected AbstractCorrelation() {
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

    public static abstract class AbstractCorrelationPK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -2160693859036816154L;

        public AbstractCorrelationPK() {
        }
        
        public abstract int hashCode();
        
        public abstract boolean equals(Object obj);
    }
}
