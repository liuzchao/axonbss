/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ai.bss.query.party;

import org.springframework.data.annotation.Id;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * @author Lianhua Zhang
 */
@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="PARTY_TYPE",discriminatorType=DiscriminatorType.STRING)
@Table(name="PT_PARTY")
public abstract class PartyEntry {

    @Id
    @javax.persistence.Id
    private String partyId;
    private String name;
    private String type;
    private String state ;
    
    public PartyEntry(){}
    
    protected PartyEntry(String identifier,String name,String type){
    	this.partyId=identifier;
    	this.name=name;
    	this.type=type;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String identifier) {
        this.partyId = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}