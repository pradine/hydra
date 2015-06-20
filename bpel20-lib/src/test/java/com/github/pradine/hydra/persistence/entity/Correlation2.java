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


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(Correlation2.Correlation2PK.class)
public class Correlation2 extends AbstractCorrelation {
    @Id
    private Double auctionId;
    
    @Id
    private Double activityId;

    public Correlation2() {
        super();
    }

    public Correlation2(Double auctionId, Double activityId) {
        super();
        this.auctionId = auctionId;
        this.activityId = activityId;
    }

    public Double getAuctionId() {
        return auctionId;
    }

    public Double getActivityId() {
        return activityId;
    }

    @Override
    public String toString() {
        return "Correlation2 [auctionId=" + auctionId + ", activityId="
                + activityId + ", processId=" + processId + "]";
    }

    public static class Correlation2PK extends AbstractCorrelationPK {
        /**
         * 
         */
        private static final long serialVersionUID = -1954117817326060711L;

        private Double auctionId;
        
        private Double activityId;
        
        public Correlation2PK() {
            super();
        }
        
        public Correlation2PK(Double auctionId, Double activityId) {
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
            Correlation2PK other = (Correlation2PK) obj;
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
            return "Correlation2PK [auctionId=" + auctionId + ", activityId="
                    + activityId + "]";
        }
    }
}
