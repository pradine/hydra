package com.github.pradine.hydra.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(Correlation3.Correlation3PK.class)
public class Correlation3 extends AbstractCorrelation {
    @Id
    private Long auctionId;
    
    @Id
    private String activityId;

    public Correlation3() {
        super();
    }

    public Correlation3(Long auctionId, String activityId) {
        super();
        this.auctionId = auctionId;
        this.activityId = activityId;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public String getActivityId() {
        return activityId;
    }

    @Override
    public String toString() {
        return "Correlation3 [auctionId=" + auctionId + ", activityId="
                + activityId + ", processId=" + processId + "]";
    }

    public static class Correlation3PK extends AbstractCorrelationPK {
        /**
         * 
         */
        private static final long serialVersionUID = 6191937770560052646L;

        private Long auctionId;
        
        private String activityId;
        
        public Correlation3PK() {
            super();
        }
        
        public Correlation3PK(Long auctionId, String activityId) {
            super();
            this.auctionId = auctionId;
            this.activityId = activityId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((activityId == null) ? 0 : activityId.hashCode());
            result = prime * result
                    + ((auctionId == null) ? 0 : auctionId.hashCode());
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
            Correlation3PK other = (Correlation3PK) obj;
            if (activityId == null) {
                if (other.activityId != null)
                    return false;
            }
            else if (!activityId.equals(other.activityId))
                return false;
            if (auctionId == null) {
                if (other.auctionId != null)
                    return false;
            }
            else if (!auctionId.equals(other.auctionId))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Correlation3PK [auctionId=" + auctionId + ", activityId="
                    + activityId + "]";
        }
    }
}
