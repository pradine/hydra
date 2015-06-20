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

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class OutboundDestination {
    @TableGenerator(name = "OUTBOUND_ID", table = "ID_GENERATOR",
            pkColumnName = "PRIMARY_KEY", valueColumnName = "VALUE",
            pkColumnValue = "outboundId")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OUTBOUND_ID")
    private Long id;

    @Version
    private Long version;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar departureTime;
    
    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "dataId", referencedColumnName = "id")
    private Data data;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    protected void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
    @PrePersist
    protected void prePersist() {
        if (departureTime == null)
            departureTime = Calendar.getInstance();
    }

    @Override
    public String toString() {
        return "OutboundDestination [id=" + id + ", version=" + version
                + ", departureTime=" + departureTime + "]";
    }
}
