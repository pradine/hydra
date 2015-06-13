package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
@IdClass(FlowContext.FlowContextPK.class)
public class FlowContext {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeContextId", referencedColumnName = "id")
    private ScopeContext scopeContext;
    
    @Id
    private String flowId;
    
    @Id
    private Integer counter;
    
    @Version
    private Long version;
    
    //TODO We may want to keep track of any nested (isolated) scopes as well
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flowContext")
    @MapKey(name = "name")
    private Map<String, Link> links;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    @MapKey
    private Map<FlowContextPK, FlowContext> children;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "PARENT_SCOPE_CONTEXT_ID", referencedColumnName = "scopeContextId"),
        @JoinColumn(name = "PARENT_FLOW_ID", referencedColumnName = "flowId"),
        @JoinColumn(name = "PARENT_COUNTER", referencedColumnName = "counter")
    })
    private FlowContext parent;
    
    protected FlowContext() {
    }
    
    public FlowContext(String flowId) {
        this(flowId, 0);
    }

    public FlowContext(String flowId, Integer counter) {
        this.flowId = flowId;
        this.counter = counter;
    }

    public String getFlowId() {
        return flowId;
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

    public FlowContext getParent() {
        return parent;
    }

    protected void setParent(FlowContext parent) {
        this.parent = parent;
    }
    
    protected void setLinks(Map<String, Link> links) {
        this.links = links;
    }
    
    protected Map<String, Link> getLinks() {
        return links;
    }

    /**
     * Adds a <code>Link</code> to the current flow. This method also sets the
     * flow on the added <code>Link</code>.
     * 
     * @param link the <code>Link</code> to be added.
     * @throws IllegalArgumentException if the <code>Link</code> is <code>null</code>
     */
    public void addLink(Link link) {
        if (link == null)
            throw new IllegalArgumentException("Null link");
        
        Map<String, Link> links = getLinks();
        
        if (links == null) {
            links = new ConcurrentHashMap<String, Link>();
            setLinks(links);
        }
        
        links.put(link.getName(), link);
        link.setFlowContext(this);
    }
    
    /**
     * This method returns a link that matches the specified name. If a matching link
     * is not found in the current flow then it will recursively check the parent
     * flows until either a link is found, or a parent flow is <code>null</code>.
     * 
     * @param name the link name
     * @return either the first link found that matches the specified name, or
     * <code>null</code> if such a link cannot be found.
     */
    public Link getLink(String name) {
        Map<String, Link> links = getLinks();
        Link link = null;
        
        if (links != null && links.containsKey(name))
            link = links.get(name);
        else if (parent != null)
            link = parent.getLink(name);
        
        return link;
    }

    protected Map<FlowContextPK, FlowContext> getChildren() {
        return children;
    }
    
    protected void setChildren(Map<FlowContextPK, FlowContext> children) {
        this.children = children;
    }
    
    /**
     * This method returns a child flow based on the parameters passed to it.
     * 
     * @param scopeContext the id of the scope
     * @param flowId the flow id
     * @return either a <code>FlowContext</code>, or <code>null</code> if a flow
     * cannot be found.
     * @throws IllegalArgumentException if the <code>scopeContext</code> or the
     * <code>flowId</code> is <code>null</code>
     * @see #getChild(Long, String, Integer)
     */
    public FlowContext getChild(Long scopeContext, String flowId) {
        return getChild(scopeContext, flowId, 0);
    }
    
    /**
     * This method returns a child flow based on the parameters passed to it.
     * 
     * @param scopeContext the id of the scope
     * @param flowId the flow id
     * @param counter the loop counter, if the flow is contained in one of the
     * looping activities.
     * @return either a <code>FlowContext</code>, or <code>null</code> if a flow
     * cannot be found.
     * @throws IllegalArgumentException if the <code>scopeContext</code>, the
     * <code>flowId</code> or the <code>counter</code> is <code>null</code>
     * @see #getChild(Long, String)
     */
    public FlowContext getChild(Long scopeContext, String flowId, Integer counter) {
        if (scopeContext == null)
            throw new IllegalArgumentException("Null scope context");
        
        if (flowId == null)
            throw new IllegalArgumentException("Null flow id");
        
        if (counter == null)
            throw new IllegalArgumentException("Null counter");

        Map<FlowContextPK, FlowContext> children = getChildren();
        FlowContextPK key = new FlowContextPK(scopeContext, flowId, counter);
        FlowContext child = null;
        
        if (children != null)
            child = children.get(key);
        
        return child;
    }
    
    /**
     * Adds a new child flow to the current one. This method must only be called
     * after {@link ScopeContext#addFlowContext(FlowContext)} is called, passing
     * the child flow as an argument. This method also sets the parent flow on
     * the child flow.
     * 
     * @param child the child flow
     * @throws IllegalArgumentException if the child flow is <code>null</code>
     */
    public void addChild(FlowContext child) {
        if (child == null)
            throw new IllegalArgumentException("Null flow context");
        
        Map<FlowContextPK, FlowContext> children = getChildren();
        
        if (children == null) {
            children = new ConcurrentHashMap<FlowContextPK, FlowContext>();
            setChildren(children);
        }
        
        children.put(new FlowContextPK(child.getScopeContext().getId(), child.getFlowId(), child.getCounter()), child);
        child.setParent(this);
    }
    
    @Override
    public String toString() {
        return "FlowContext [scopeContext=" + scopeContext + ", flowId="
                + flowId + ", counter=" + counter + ", version=" + version
                + "]";
    }

    public static class FlowContextPK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 7691159694652676290L;

        private Long scopeContext;

        private String flowId;
        
        private Integer counter;
        
        public FlowContextPK() {
        }

        public FlowContextPK(Long scopeContext, String flowId, Integer counter) {
            this.scopeContext = scopeContext;
            this.flowId = flowId;
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
                    + ((flowId == null) ? 0 : flowId.hashCode());
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
            FlowContextPK other = (FlowContextPK) obj;
            if (flowId == null) {
                if (other.flowId != null)
                    return false;
            } else if (!flowId.equals(other.flowId))
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
            return "FlowContextPK [scopeContext=" + scopeContext + ", flowId="
                    + flowId + ", counter=" + counter + "]";
        }
    }
}
