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
