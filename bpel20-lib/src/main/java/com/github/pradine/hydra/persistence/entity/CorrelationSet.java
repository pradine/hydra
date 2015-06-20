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
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.xml.namespace.QName;

@Entity
@IdClass(CorrelationSet.CorrelationSetPK.class)
public class CorrelationSet {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;
    
    @Id
    private String name;
    
    @Version
    private Long version;
    
    @Column(nullable = false)
    private Class<?> correlation;
    
    private Class<?> correlationPK;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "properties")   
    private List<QName> properties;
    
    protected CorrelationSet() {
    }
    
    public CorrelationSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public Class<?> getCorrelation() {
        return correlation;
    }

    public void setCorrelation(Class<?> correlation) {
        this.correlation = correlation;
    }

    public Class<?> getCorrelationPK() {
        return correlationPK;
    }

    public void setCorrelationPK(Class<?> correlationPK) {
        this.correlationPK = correlationPK;
    }

    public List<QName> getProperties() {
        return properties;
    }

    public void setProperties(List<QName> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "CorrelationSet [scopeContext=" + scopeContext + ", name="
                + name + ", version=" + version + ", correlation="
                + correlation + ", correlationPK=" + correlationPK
                + ", properties=" + properties + "]";
    }

    public static class CorrelationSetPK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1252386150875573157L;

        private Long scopeContext;

        private String name;

        public CorrelationSetPK() {
        }

        public CorrelationSetPK(Long scopeContext, String name) {
            this.scopeContext = scopeContext;
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((scopeContext == null) ? 0 : scopeContext.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CorrelationSetPK other = (CorrelationSetPK) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
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
            return "CorrelationSetPK [scopeContext=" + scopeContext + ", name="
                    + name + "]";
        }
    }
}
