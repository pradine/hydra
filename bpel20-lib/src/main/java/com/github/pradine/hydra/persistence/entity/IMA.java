package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IMA implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7604935356217077717L;

    @Column(nullable = false, updatable = false)
    private String activityId;
    
    @Column(nullable = false)
    private Integer counter;
    
    protected IMA() {
    }
    
    public IMA(String activityId) {
        this.activityId = activityId;
        this.counter = new Integer(0);
    }

    public String getActivityId() {
        return activityId;
    }

    public Integer getCounter() {
        return counter;
    }
    
    public void increment() {
        ++counter;
    }
    
    public void decrement() {
        --counter;
    }

    @Override
    public String toString() {
        return "IMA [activityId=" + activityId + ", counter=" + counter + "]";
    }
}
