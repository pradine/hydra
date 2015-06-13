package com.github.pradine.hydra.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.xml.namespace.QName;

import com.github.pradine.hydra.persistence.entity.ActivityState.ActivityStatePK;
import com.github.pradine.hydra.persistence.entity.FlowContext.FlowContextPK;

@Entity
public class ScopeContext {
    @TableGenerator(name = "CONTEXT_ID", table = "ID_GENERATOR",
            pkColumnName = "PRIMARY_KEY", valueColumnName = "VALUE",
            pkColumnValue = "contextId")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CONTEXT_ID")
    private Long id;
    
    @Version
    private Long version;
    
    @Column(nullable = false, length = 36)
    private UUID processId;
    
    @Column(nullable = false)
    private String scopeId;
    
    @Column(nullable = false)
    private Boolean isolated;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext")
    @MapKey(name = "name")
    private Map<String, Variable> variables;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext")
    @MapKey(name = "name")
    private Map<String, PartnerLink> partnerLinks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext")
    @MapKey(name = "name")
    private Map<String, MessageExchange> messageExchanges;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext")
    @MapKey(name = "name")
    private Map<String, CorrelationSet> correlationSets;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext", orphanRemoval = true)
    private List<Timer> timers;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext")
    @MapKey
    private Map<FlowContextPK, FlowContext> flowContexts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scopeContext")
    @MapKey
    private Map<ActivityStatePK, ActivityState> activityStates;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    @MapKey(name = "scopeId")
    private Map<String, ScopeContext> children;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "id")
    private ScopeContext parent;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "eventHandlerIds")
    private List<String> eventHandlerIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "faultHandlerIds")
    private List<String> faultHandlerIds;
    
    private String compensationHandlerId;
    
    private String terminationHandlerId;

    private QName faultName;
    
    private String faultVariableName;

    /**
     * Get the scope id.
     * 
     * @return either the scope id or <code>null</code>. This method will return
     * the scope id shortly after the entity has been persisted for the first time.
     */
    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    protected void setProcessId(UUID processId) {
        this.processId = processId;
    }

    public UUID getProcessId() {
        return processId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public Boolean isIsolated() {
        return isolated;
    }

    public void setIsolated(Boolean isolated) {
        this.isolated = isolated;
    }

    public ScopeContext getParent() {
        return parent;
    }

    protected void setParent(ScopeContext parent) {
        this.parent = parent;
    }
    
    protected void setMessageExchanges(Map<String, MessageExchange> messageExchanges) {
        this.messageExchanges = messageExchanges;
    }
    
    protected Map<String, MessageExchange> getMessageExchanges() {
        return messageExchanges;
    }
    
    /**
     * Adds a new <code>MessageExchange</code> to the current scope. This method
     * also sets the scope on the added <code>MessageExchange</code>.
     * 
     * @param messageExchange the <code>MessaeExchange</code> to be added.
     * @throws IllegalArgumentException if the <code>MessageExchange</code> is
     * <code>null</code>
     */
    public void addMessageExchange(MessageExchange messageExchange) {
        if (messageExchange == null)
            throw new IllegalArgumentException("Null message exchange");
        
        Map<String, MessageExchange> messageExchanges = getMessageExchanges();
        
        if (messageExchanges == null) {
            messageExchanges = new HashMap<String, MessageExchange>();
            setMessageExchanges(messageExchanges);
        }
        
        messageExchanges.put(messageExchange.getName(), messageExchange);
        messageExchange.setScopeContext(this);
    }
    
    public MessageExchange getMessageExchange(String name) {
        Map<String, MessageExchange> messageExchanges = getMessageExchanges();
        MessageExchange messageExchange = null;
        
        if (messageExchanges != null && messageExchanges.containsKey(name))
            messageExchange = messageExchanges.get(name);
        else if (parent != null)
            messageExchange = parent.getMessageExchange(name);
        
        return messageExchange;
    }
    
    protected void setVariables(Map<String, Variable> variables) {
        this.variables = variables;
    }
    
    protected Map<String, Variable> getVariables() {
        return variables;
    }
    
    /**
     * Adds a new <code>Variable</code> to the current scope. This method also sets
     * the scope on the added <code>Variable</code>.
     * 
     * @param variable the <code>Variable</code> to be added.
     * @throws IllegalArgumentException if the <code>Variable</code> is <code>null</code>
     */
    public void addVariable(Variable variable) {
        if (variable == null)
            throw new IllegalArgumentException("Null variable");
        
        Map<String, Variable> variables = getVariables();
        
        if (variables == null) {
            variables = new HashMap<String, Variable>();
            setVariables(variables);
        }
        
        variables.put(variable.getName(), variable);
        variable.setScopeContext(this);
    }
    
    public Variable getVariable(String name) {
        Map<String, Variable> variables = getVariables();
        Variable variable = null;
        
        if (variables != null && variables.containsKey(name))
            variable = variables.get(name);
        else if (parent != null)
            variable = parent.getVariable(name);
        
        return variable;
    }
    
    protected void setPartnerLinks(Map<String, PartnerLink> partnerLinks) {
        this.partnerLinks = partnerLinks;
    }
    
    protected Map<String, PartnerLink> getPartnerLinks() {
        return partnerLinks;
    }
    
    /**
     * Adds a new <code>PartnerLink</code> to the current scope. This method
     * also sets the scope on the added <code>PartnerLink</code>.
     * 
     * @param partnerLink the <code>PartnerLink</code> to be added.
     * @throws IllegalArgumentException if the <code>PartnerLink</code> is
     * <code>null</code>
     */
    public void addPartnerLink(PartnerLink partnerLink) {
        if (partnerLink == null)
            throw new IllegalArgumentException("Null partner link");
        
        Map<String, PartnerLink> partnerLinks = getPartnerLinks();
        
        if (partnerLinks == null) {
            partnerLinks = new HashMap<String, PartnerLink>();
            setPartnerLinks(partnerLinks);
        }
        
        partnerLinks.put(partnerLink.getName(), partnerLink);
        partnerLink.setScopeContext(this);
    }
    
    public PartnerLink getPartnerLink(String name) {
        Map<String, PartnerLink> partnerLinks = getPartnerLinks();
        PartnerLink partnerLink = null;
        
        if (partnerLinks != null && partnerLinks.containsKey(name))
            partnerLink = partnerLinks.get(name);
        else if (parent != null)
            partnerLink = parent.getPartnerLink(name);
        
        return partnerLink;
    }
    
    protected void setFlowContexts(Map<FlowContextPK, FlowContext> flowContexts) {
        this.flowContexts = flowContexts;
    }
    
    protected Map<FlowContextPK, FlowContext> getFlowContexts() {
        return flowContexts;
    }
    
    /**
     * Adds a new flow to the current scope. This method also sets the scope
     * on the added flow.
     * 
     * @param flowContext the flow to be added.
     * @throws IllegalArgumentException if the flow is <code>null</code>
     */
    public void addFlowContext(FlowContext flowContext) {
        if (flowContext == null)
            throw new IllegalArgumentException("Null flow context");
        
        Map<FlowContextPK, FlowContext> flowContexts = getFlowContexts();
        
        if (flowContexts == null) {
            flowContexts = new HashMap<FlowContextPK, FlowContext>();
            setFlowContexts(flowContexts);
        }
        
        flowContexts.put(new FlowContextPK(id, flowContext.getFlowId(), flowContext.getCounter()), flowContext);
        flowContext.setScopeContext(this);
    }

    /**
     * Get a flow that is within the current scope. This method must not be used
     * until {@link #getId()} no longer returns <code>null</code>.
     * 
     * @param flowId the flow id
     * @return either a <code>FlowContext</code>, or <code>null</code> if a flow
     * cannot be found.
     * @throws IllegalArgumentException if the <code>flowId</code> is
     * <code>null</code>
     * @see #getFlowContext(String, Integer)
     */
    public FlowContext getFlowContext(String flowId) {
        return getFlowContext(flowId, 0);
    }
    
    /**
     * Get a flow that is within the current scope. This method must not be used
     * until {@link #getId()} no longer returns <code>null</code>.
     * 
     * @param flowId the flow id
     * @param counter the loop counter, if the flow is contained in one of the
     * looping activities.
     * @return either a <code>FlowContext</code>, or <code>null</code> if a flow
     * cannot be found.
     * @throws IllegalArgumentException if the <code>flowId</code> or the
     * <code>counter</code> is <code>null</code>
     * @see #getFlowContext(String)
     */
    public FlowContext getFlowContext(String flowId, Integer counter) {
        if (id == null)
            throw new IllegalArgumentException("Null id");        
        
        if (flowId == null)
            throw new IllegalArgumentException("Null flow id");
        
        if (counter == null)
            throw new IllegalArgumentException("Null counter");

        Map<FlowContextPK, FlowContext> flowContexts = getFlowContexts();
        FlowContextPK key = new FlowContextPK(id, flowId, counter);
        FlowContext flowContext = null;
        
        if (flowContexts != null)
            flowContext = flowContexts.get(key);
        
        return flowContext;
    }

    protected Map<ActivityStatePK, ActivityState> getActivityStates() {
        return activityStates;
    }

    protected void setActivityStates(Map<ActivityStatePK, ActivityState> activityStates) {
        this.activityStates = activityStates;
    }
    
    /**
     * Adds a new <code>ActivityState</code> to the current scope. This method
     * also sets the scope on the added <code>ActivityState</code>.
     * 
     * @param activityState the <code>ActivityState</code> to be added.
     * @throws IllegalArgumentException if the <code>ActivityState</code> is
     * <code>null</code>
     */
    public void addActivityState(ActivityState activityState) {
        if (activityState == null)
            throw new IllegalArgumentException("Null activity state");
        
        Map<ActivityStatePK, ActivityState> activityStates = getActivityStates();
        
        if (activityStates == null) {
            activityStates = new HashMap<ActivityStatePK, ActivityState>();
            setActivityStates(activityStates);
        }
        
        activityStates.put(new ActivityStatePK(id, activityState.getActivityId(), activityState.getCounter()), activityState);
        activityState.setScopeContext(this);
    }

    /**
     * Get the activity state for an activity that is within the current scope.
     * This method must not be used until {@link #getId()} no longer returns
     * <code>null</code>.
     * 
     * @param activityId the activity id
     * @return either an <code>ActivityState</code>, or <code>null</code> if the
     * state cannot be found.
     * @throws IllegalArgumentException if the <code>activityId</code> is
     * <code>null</code>
     * @see #getActivityState(String, Integer)
     */
    public ActivityState getActivityState(String activtyId) {
        return getActivityState(activtyId, 0);
    }
    
    /**
     * Get the activity state for an activity that is within the current scope.
     * This method must not be used until {@link #getId()} no longer returns
     * <code>null</code>.
     * 
     * @param activityId the activity id
     * @param counter the loop counter, if the activity is contained in one of the
     * looping activities.
     * @return either an <code>ActivityState</code>, or <code>null</code> if the
     * state cannot be found.
     * @throws IllegalArgumentException if the <code>activityId</code> or the
     * <code>counter</code> is <code>null</code>
     * @see #getActivityState(String)
     */
    public ActivityState getActivityState(String activityId, Integer counter) {
        if (id == null)
            throw new IllegalArgumentException("Null id");        
        
        if (activityId == null)
            throw new IllegalArgumentException("Null activity id");
        
        if (counter == null)
            throw new IllegalArgumentException("Null counter");

        Map<ActivityStatePK, ActivityState> activityStates = getActivityStates();
        ActivityStatePK key = new ActivityStatePK(id, activityId, counter);
        ActivityState activityState = null;
        
        if (activityStates != null)
            activityState = activityStates.get(key);
        
        return activityState;
    }
    
    protected Map<String, CorrelationSet> getCorrelationSets() {
        return correlationSets;
    }

    protected void setCorrelationSets(Map<String, CorrelationSet> correlationSets) {
        this.correlationSets = correlationSets;
    }
    
    /**
     * Adds a new <code>CorrelationSet</code> to the current scope. This method
     * also sets the scope on the added <code>CorrelationSet</code>.
     * 
     * @param correlationSet the <code>CorrelaionSet</code> to be added.
     * @throws IllegalArgumentException if the <code>CorrelationSer</code> is
     * <code>null</code>
     */
    public void addCorrelationSet(CorrelationSet correlationSet) {
        if (correlationSet == null)
            throw new IllegalArgumentException("Null correlation set");
        
        Map<String, CorrelationSet> correlationSets = getCorrelationSets();
        
        if (correlationSets == null) {
            correlationSets = new HashMap<String, CorrelationSet>();
            setCorrelationSets(correlationSets);
        }
        
        correlationSets.put(correlationSet.getName(), correlationSet);
        correlationSet.setScopeContext(this);
    }
    
    public CorrelationSet getCorrelationSet(String name) {
        Map<String, CorrelationSet> correlationSets = getCorrelationSets();
        CorrelationSet correlationSet = null;
        
        if (correlationSets != null && correlationSets.containsKey(name))
            correlationSet = correlationSets.get(name);
        else if (parent != null)
            correlationSet = parent.getCorrelationSet(name);
        
        return correlationSet;
    }

    protected Map<String, ScopeContext> getChildren() {
        return children;
    }
    
    protected void setChildren(Map<String, ScopeContext> children) {
        this.children = children;
    }
    
    public ScopeContext getChild(String scopeId) {
        ScopeContext child = null;
        Map<String, ScopeContext> children = getChildren();
        
        if (children != null)
            child = children.get(scopeId);
        
        return child;
    }
    
    /**
     * Adds a new child scope to the current one. This method must only be called
     * after {@link ScopeContext#setScopeId(String)} is invoked on the child scope.
     * This method also sets the parent scope and the process id on the child scope.
     * 
     * @param child the child scope
     * @throws IllegalArgumentException if the child scope is <code>null</code>
     */
    public void addChild(ScopeContext child) {
        if (child == null)
            throw new IllegalArgumentException("Null scope context");
        
        Map<String, ScopeContext> children = getChildren();
        
        if (children == null) {
            children = new HashMap<String, ScopeContext>();
            setChildren(children);
        }
        
        children.put(child.getScopeId(), child);
        child.setParent(this);
        child.setProcessId(processId);
    }

    /**
     * This method can be used to obtain a list of <code>Timer</code>
     * entities, or to remove selected entities from the list. It must not
     * be used to add new entities to the list, as {@link #addTimer(Timer)}
     * must be used instead.
     * 
     * @return a <code>List</code> containing <code>Timer</code> entities.
     */
    public List<Timer> getTimers() {
        return timers;
    }

    protected void setTimers(List<Timer> timers) {
        this.timers = timers;
    }
    
    /**
     * Adds a new <code>Timer</code> to the current scope. This method also sets
     * the scope on the added <code>Timer</code>.
     * 
     * @param timer the <code>Timer</code> to be added.
     * @throws IllegalArgumentException if the <code>Timer</code> is <code>null</code>
     */
    public void addTimer(Timer timer) {
        if (timer == null)
            throw new IllegalArgumentException("Null timer");
        
        List<Timer> timers = getTimers();
        
        if (timers == null) {
            timers = new ArrayList<Timer>();
            setTimers(timers);
        }
        
        timers.add(timer);
        timer.setScopeContext(this);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public QName getFaultName() {
        return faultName;
    }

    public void setFaultName(QName faultName) {
        this.faultName = faultName;
    }

    public String getFaultVariableName() {
        return faultVariableName;
    }

    public void setFaultVariableName(String faultVariableName) {
        this.faultVariableName = faultVariableName;
    }

    public List<String> getEventHandlerIds() {
        return eventHandlerIds;
    }

    public List<String> getFaultHandlerIds() {
        return faultHandlerIds;
    }

    public void setFaultHandlerIds(List<String> faultHandlerIds) {
        this.faultHandlerIds = faultHandlerIds;
    }

    public void setEventHandlerIds(List<String> eventHandlerIds) {
        this.eventHandlerIds = eventHandlerIds;
    }

    public String getCompensationHandlerId() {
        return compensationHandlerId;
    }

    public void setCompensationHandlerId(String compensationHandlerId) {
        this.compensationHandlerId = compensationHandlerId;
    }

    public String getTerminationHandlerId() {
        return terminationHandlerId;
    }

    public void setTerminationHandlerId(String terminationHandlerId) {
        this.terminationHandlerId = terminationHandlerId;
    }

    @Override
    public String toString() {
        return "ScopeContext [id=" + id + ", version=" + version
                + ", processId=" + processId + ", scopeId=" + scopeId
                + ", isolated=" + isolated + ", owner=" + owner + "]";
    }
}
