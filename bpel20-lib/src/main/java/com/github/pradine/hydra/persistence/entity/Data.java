package com.github.pradine.hydra.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.RollbackException;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import net.sf.saxon.s9api.XdmNode;

/**
 * <p>This class represents the link to any binary data held in the database,
 * e.g. message data. This class is designed to represent the three types of
 * message as defined in the WS-BPEL 2.0 specification, i.e. XML schema element,
 * XML schema type, and WSDL message type. These types are handled differently
 * as follows:</p>
 * <p>The XML schema element and XML schema type are handled in a similar manner
 * in that there will be a single <code>Data</code> entity that references the
 * binary data. The content of the binary data will be different in that the XML
 * schema element requires the message to use the same element name as specified
 * in the XML schema, whereas the a XML schema type message can have any element
 * name, so long as it is of the correct type.</p>
 * <p>The WSDL message type, however, uses multiple <code>Data</code> entities in
 * order to represent a single message. There will be a single <code>Data</code>
 * entity that references a number of child <code>Data</code> entities, one for
 * each WSDL message part. Each of these child <Data> entities will refer to the
 * binary data for only their part of the message. As far as the binary data is
 * concerned, each of these parts can be considered to be similar to an XML
 * schema type message, as described above.</p>
 */
@Entity
public class Data {
    @TableGenerator(name = "DATA_ID", table = "ID_GENERATOR",
            pkColumnName = "PRIMARY_KEY", valueColumnName = "VALUE",
            pkColumnValue = "dataId")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DATA_ID")
    private Long id;

    @Version
    private Long version;
    
    private String partName;
    
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = true)
    private XdmNode node;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "partName")
    private Map<String, Data> children;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "id")
    private Data parent;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    /**
     * The the part name for the WSDL message part that this <code>Data</code>
     * entity represents.
     * 
     * @return the part name, or <code>null</code>, if the <code>Data</code>
     * entity does not represent a WSDL message part.
     */
    public String getPartName() {
        return partName;
    }

    /**
     * The part name must only be set on the child <code>Data</code> entities
     * that represent the different parts of a WSDL message type.
     * 
     * @param partName the part name of a WSDL message part
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * Get the message data.
     * 
     * @return if the <code>Data</code> entity represents a WSDL message part
     * then this method returns the message data, or <code>null</code> if there
     * is no data. If the entity represents the parent of the child entities
     * then this method returns <code>null</code>. If the entity represents a
     * XML schema element or type then this method returns the binary message,
     * or <code>null</code> if there is no data.
     */
    public XdmNode getNode() {
        return node;
    }

    /**
     * Set the message data on the <code>Data</code> entity. If the message is
     * a WSDL type message then this method must only be called on the child
     * <code>Data</code> entities. If the message is an XML schema element or
     * type message then this method can be called on the entity.
     * 
     * @param node the message data.
     */
    public void setNode(XdmNode node) {
        this.node = node;
    }

    /**
     * This method can be used to obtain a map of child <code>Data</code>
     * entities, or to remove selected entities from the map. It must not
     * be used to add new entities to the map, as {@link #addChild(Data)}
     * must be used instead. This method is only used for WSDL message
     * types.
     * 
     * @return a <code>Map</code> containing the relation part names to
     * <code>Data</code> entities.
     */
    //TODO Should this be a public method?
    public Map<String, Data> getChildren() {
        return children;
    }

    protected void setChildren(Map<String, Data> children) {
        this.children = children;
    }
    
    /**
     * This method returns a <code>Data</code> entity based on the parameter
     * passed to it. This method is only used for WSDL message types.
     * 
     * @param partName the part name related to the <code>Data</code> entity.
     * @return a <code>Data</code> entity.
     * @throws IllegalArgumentException if the part name is <code>null</code>.
     */
    public Data getChild(String partName) {
        if (partName == null)
            throw new IllegalArgumentException("Null part name");
        
        Map<String, Data> children = getChildren();
        Data child = null;
        
        if (children != null)
            child = children.get(partName);
        
        return child;
    }
    
    /**
     * Adds a new child <code>Data</code> entity to the current one. This method must
     * only be called after {@link Data#setPartName(String)} is invoked on the child.
     * This method also sets the parent <code>Data</code> entity on the child. This
     * method is only used for WSDL message types.
     * 
     * @param child the child <code>Data</code> entity to be added.
     * @throws IllegalArgumentException if the child is <code>null</code>
     */
    public void addChild(Data child) {
        if (child == null)
            throw new IllegalArgumentException("Null data");
        
        Map<String, Data> children = getChildren();
        
        if (children == null) {
            children = new HashMap<String, Data>();
            setChildren(children);
        }
        
        children.put(child.getPartName(), child);
        child.setParent(this);
    }

    /**
     * Get the parent <code>Data</code> entity.
     * 
     * @return if the <code>Data</code> entity represents a WSDL message part
     * then this method returns the parent. If the entity represents the parent
     * of the child entities then this method returns <code>null</code>. If the
     * entity represents a XML schema element or type then this method returns
     * <code>null</code>.
     */
    public Data getParent() {
        return parent;
    }

    protected void setParent(Data parent) {
        this.parent = parent;
    }
    
    @PrePersist
    protected void prePersist() {
        validate();
    }
    
    @PreUpdate
    protected void preUpdate() {
        validate();
    }
    
    private void validate() {
        try {
            if (parent == null) {
                assert partName == null : "The return value from Data.getPartName() is not null.";
                Map<String, Data> children = getChildren();

                if (children != null)
                    assert node == null : "The return value from Data.getNode() is not null.";
            }
            else {
                assert partName != null : "The return value from Data.getPartName() is null.";
                Map<String, Data> children = getChildren();
                
                assert children == null || children.isEmpty() : "The return value from Data.getChildren() is not empty, or null.";
            }
        }
        catch (AssertionError ae) {
            throw new RollbackException(ae);
        }
    }

    @Override
    public String toString() {
        return "Data [id=" + id + ", version=" + version + ", partName="
                + partName + "]";
    }
}
