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
