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

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.xml.namespace.QName;

@Entity
public class WSDL11ToIMA {
    @TableGenerator(name = "WSDL11_ID", table = "ID_GENERATOR",
            pkColumnName = "PRIMARY_KEY", valueColumnName = "VALUE",
            pkColumnValue = "wsdl11Id")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "WSDL11_ID")
    private Long id;
    
    @Column(nullable = false)
    private QName portType;
    
    @Column(nullable = false)
    private String operation;
    
    @Version
    private Long version;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "IMA",
    joinColumns = { @JoinColumn(name = "OWNER_ID", referencedColumnName = "id") })   
    private List<IMA> imas;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public QName getPortType() {
        return portType;
    }

    public void setPortType(QName portType) {
        this.portType = portType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<IMA> getIMAs() {
        return imas;
    }

    public void setIMAs(List<IMA> imas) {
        this.imas = imas;
    }

    @Override
    public String toString() {
        return "WSDL11ToIMA [id=" + id + ", portType=" + portType
                + ", operation=" + operation + ", version=" + version
                + ", imas=" + imas + "]";
    }
}
