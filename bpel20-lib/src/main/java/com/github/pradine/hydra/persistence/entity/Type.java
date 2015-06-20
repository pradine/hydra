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
