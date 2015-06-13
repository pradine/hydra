package com.github.pradine.hydra.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.namespace.QName;

@Embeddable
public class Type implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8497995258974850363L;

    public static enum Category { WSDL_MESSAGE, XSD_ELEMENT, XSD_TYPE }
    
    @Column(nullable = false, name = "typeName")
    private QName name;
    
    @Column(nullable = false)
    private Category category;

    protected Type() {
    }

    public Type(QName name, Category category) {
        this.name = name;
        this.category = category;
    }

    public QName getName() {
        return name;
    }

    @Enumerated(EnumType.STRING)
    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Type [qname=" + name + ", category=" + category + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Type other = (Type) obj;
        if (category != other.category)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
