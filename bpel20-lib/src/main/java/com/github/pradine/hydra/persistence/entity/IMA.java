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
