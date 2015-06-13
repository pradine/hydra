package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.xml.namespace.QName;

import net.sf.saxon.s9api.XdmNode;

@Entity
@IdClass(PartnerLink.PartnerLinkPK.class)
public class PartnerLink {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;
    
    @Id
    private String name;
    
    @Version
    private Long version;
    
    @Lob
    private XdmNode partnerEPR;
    
    @Lob
    @Column(nullable = false)
    private XdmNode myEPR;
    
    @Column(nullable = false)
    private Boolean copyOnWrite;
    
    @Column(nullable = false)
    private QName typeName;

    private String myRole;
    
    private String partnerRole;
    
    private Boolean initializePartnerRole;

    protected PartnerLink() {
    }
    
    public PartnerLink(String name) {
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

    public XdmNode getPartnerEPR() {
        return partnerEPR;
    }
    
    public void setPartnerEPR(XdmNode partnerEPR) {
        this.partnerEPR = partnerEPR;
    }

    public XdmNode getMyEPR() {
        return myEPR;
    }

    public void setMyEPR(XdmNode myEPR) {
        this.myEPR = myEPR;
    }
    
    public QName getTypeName() {
        return typeName;
    }

    public void setTypeName(QName typeName) {
        this.typeName = typeName;
    }

    public Boolean isCopyOnWrite() {
        return copyOnWrite;
    }

    public void setCopyOnWrite(Boolean copyOnWrite) {
        this.copyOnWrite = copyOnWrite;
    }

    public String getMyRole() {
        return myRole;
    }

    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }

    public String getPartnerRole() {
        return partnerRole;
    }

    public void setPartnerRole(String partnerRole) {
        this.partnerRole = partnerRole;
    }

    public Boolean isInitializePartnerRole() {
        return initializePartnerRole;
    }

    public void setInitializePartnerRole(Boolean initializePartnerRole) {
        this.initializePartnerRole = initializePartnerRole;
    }

    @Override
    public String toString() {
        return "PartnerLink [scopeContext=" + scopeContext + ", name=" + name
                + ", version=" + version + ", copyOnWrite=" + copyOnWrite
                + ", typeName=" + typeName + ", myRole=" + myRole + ", partnerRole="
                + partnerRole + ", initializePartnerRole=" + initializePartnerRole
                + "]";
    }

    public static class PartnerLinkPK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1650968449537967128L;
        
        private Long scopeContext;

        private String name;

        public PartnerLinkPK() {
        }

        public PartnerLinkPK(Long scopeContext, String name) {
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
            PartnerLinkPK other = (PartnerLinkPK) obj;
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
            return "PartnerLinkPK [scopeContext=" + scopeContext + ", name="
                    + name + "]";
        }
    }
}
