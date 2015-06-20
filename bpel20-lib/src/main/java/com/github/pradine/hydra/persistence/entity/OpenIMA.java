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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.github.pradine.hydra.persistence.entity.MessageExchange.MessageExchangePK;
import com.github.pradine.hydra.persistence.entity.PartnerLink.PartnerLinkPK;

@Entity
@IdClass(OpenIMA.OpenIMAPK.class)
public class OpenIMA {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "plContextId", referencedColumnName = "scopeContextId"),
        @JoinColumn(name = "plName", referencedColumnName = "name")
    })
    private PartnerLink partnerLink;

    @Id
    private String operation;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "meContextId", referencedColumnName = "scopeContextId"),
        @JoinColumn(name = "meName", referencedColumnName = "name")
    })
    private MessageExchange messageExchange;
    
    @Version
    private Long version;
    
    protected OpenIMA() {
    }
    
    public OpenIMA(PartnerLink partnerLink, String operation, MessageExchange messageExchange) {
        this.partnerLink = partnerLink;
        this.operation = operation;
        this.messageExchange = messageExchange;
    }

    public PartnerLink getPartnerLink() {
        return partnerLink;
    }

    public String getOperation() {
        return operation;
    }

    public MessageExchange getMessageExchange() {
        return messageExchange;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "OpenIMA [partnerLink=" + partnerLink + ", operation="
                + operation + ", messageExchange=" + messageExchange
                + ", version=" + version + "]";
    }

    public static class OpenIMAPK implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -2071455968525390962L;

        private PartnerLinkPK partnerLink;
        
        private String operation;
        
        private MessageExchangePK messageExchange;
        
        public OpenIMAPK() {
        }
        
        public OpenIMAPK(PartnerLinkPK partnerLink, String operation,
                MessageExchangePK messageExchange) {
            this.partnerLink = partnerLink;
            this.operation = operation;
            this.messageExchange = messageExchange;            
        }
        
        public OpenIMAPK(Long plContextId, String plName, String operation,
                Long meContextId, String meName) {
            this(new PartnerLinkPK(plContextId, plName), operation,
                    new MessageExchangePK(meContextId, meName));
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime
                    * result
                    + ((messageExchange == null) ? 0 : messageExchange
                            .hashCode());
            result = prime * result
                    + ((operation == null) ? 0 : operation.hashCode());
            result = prime * result
                    + ((partnerLink == null) ? 0 : partnerLink.hashCode());
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
            OpenIMAPK other = (OpenIMAPK) obj;
            if (messageExchange == null) {
                if (other.messageExchange != null)
                    return false;
            } else if (!messageExchange.equals(other.messageExchange))
                return false;
            if (operation == null) {
                if (other.operation != null)
                    return false;
            } else if (!operation.equals(other.operation))
                return false;
            if (partnerLink == null) {
                if (other.partnerLink != null)
                    return false;
            } else if (!partnerLink.equals(other.partnerLink))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "OpenIMAPK [partnerLink=" + partnerLink + ", operation="
                    + operation + ", messageExchange=" + messageExchange + "]";
        }
    }
}
