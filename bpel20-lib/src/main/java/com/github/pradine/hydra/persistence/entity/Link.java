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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.github.pradine.hydra.persistence.entity.FlowContext.FlowContextPK;

@Entity
@IdClass(Link.LinkPK.class)
public class Link {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "scopeContextId", referencedColumnName = "scopeContextId"),
        @JoinColumn(name = "flowId", referencedColumnName = "flowId"),
        @JoinColumn(name = "counter", referencedColumnName = "counter")
    })
    private FlowContext flowContext;

    @Id
    private String name;
    
    @Version
    private Long version;
    
    @Column(nullable = false)
    private Boolean copyOnWrite;
    
    private Boolean state;
    
    protected Link() {
    }
    
    public Link(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getVersion() {
        return version;
    }

    public FlowContext getFlowContext() {
        return flowContext;
    }

    protected void setFlowContext(FlowContext flowContext) {
        this.flowContext = flowContext;
    }

    public Boolean isCopyOnWrite() {
        return copyOnWrite;
    }

    public void setCopyOnWrite(Boolean copyOnWrite) {
        this.copyOnWrite = copyOnWrite;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Link [flowContext=" + flowContext + ", name=" + name
                + ", version=" + version + ", copyOnWrite=" + copyOnWrite
                + ", state=" + state + "]";
    }

    public static class LinkPK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 142046040883320744L;
        
        private FlowContextPK flowContext;

        private String name;
        
        public LinkPK() {
        }

        public LinkPK(FlowContextPK flowContext, String name) {
            this.flowContext = flowContext;
            this.name = name;
        }

        public LinkPK(Long scopeContextId, String flowContextId,
                Integer counter, String name) {
            this(new FlowContextPK(scopeContextId, flowContextId, counter),
                 name);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((flowContext == null) ? 0 : flowContext.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
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
            LinkPK other = (LinkPK) obj;
            if (flowContext == null) {
                if (other.flowContext != null)
                    return false;
            } else if (!flowContext.equals(other.flowContext))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "LinkPK [flowContext=" + flowContext + ", name=" + name
                    + "]";
        }
    }
}
