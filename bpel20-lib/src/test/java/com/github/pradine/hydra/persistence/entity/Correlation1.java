package com.github.pradine.hydra.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Correlation1 extends AbstractCorrelation {
    @Id
    private Double auctionId;

    public Correlation1() {
        super();
    }

    public Correlation1(Double auctionId) {
        super();
        this.auctionId = auctionId;
    }

    public Double getAuctinId() {
        return auctionId;
    }

    @Override
    public String toString() {
        return "Correlation1 [auctionId=" + auctionId + ", processId="
                + processId + "]";
    }
}
