package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
@IdClass(Variable.VariablePK.class)
public class Variable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;
    
    @Id
    private String name;
    
    @Version
    private Long version;
    
    @Column(nullable = false)
    private Boolean copyOnWrite;
    
    @Embedded
    private Type type;
    
    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "dataId", referencedColumnName = "id")
    private Data data;
    
    protected Variable() {
    }
    
    public Variable(String name) {
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

    public Boolean isCopyOnWrite() {
        return copyOnWrite;
    }

    public void setCopyOnWrite(Boolean copyOnWrite) {
        this.copyOnWrite = copyOnWrite;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Variable [scopeContext=" + scopeContext + ", name=" + name
                + ", version=" + version + ", copyOnWrite=" + copyOnWrite
                + ", type=" + type + "]";
    }

    public static class VariablePK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1964872125738306879L;
        
        private Long scopeContext;

        private String name;

        public VariablePK() {
        }
        
        public VariablePK(Long scopeContext, String name) {
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
            VariablePK other = (VariablePK) obj;
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
            return "VariablePK [scopeContext=" + scopeContext + ", name="
                    + name + "]";
        }
    }
}
