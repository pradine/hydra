package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
@IdClass(MessageExchange.MessageExchangePK.class)
public class MessageExchange {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;
    
    @Id
    private String name;
    
    @Version
    private Long version;

    protected MessageExchange() {
    }

    public MessageExchange(String name) {
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

    @Override
    public String toString() {
        return "MessageExchange [scopeContext=" + scopeContext + ", name="
                + name + ", version=" + version + "]";
    }

    public static class MessageExchangePK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 576743512870421993L;

        private Long scopeContext;

        private String name;
        
        public MessageExchangePK() {
        }

        public MessageExchangePK(Long scopeContext, String name) {
            this.scopeContext = scopeContext;
            this.name = name;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
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
            MessageExchangePK other = (MessageExchangePK) obj;
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
            return "MessageExchangePK [scopeContext=" + scopeContext
                    + ", name=" + name + "]";
        }
    }
}
