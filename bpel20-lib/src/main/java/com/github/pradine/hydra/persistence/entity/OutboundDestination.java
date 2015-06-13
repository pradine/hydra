package com.github.pradine.hydra.persistence.entity;

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
