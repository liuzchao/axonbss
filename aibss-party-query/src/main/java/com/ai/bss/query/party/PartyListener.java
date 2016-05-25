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

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.bss.api.party.event.IndividualCreatedEvent;
import com.ai.bss.query.party.repositories.PartyQueryRepository;

/**
 * @author Jettro Coenradie
 */
@Component
public class PartyListener {

    private PartyQueryRepository partyRepository;

    @EventHandler
    public void handleIndividualCreatedEvent(IndividualCreatedEvent event) {
        IndividualEntry PartyEntry = new IndividualEntry(event.getPartyId().toString(),event.getFirstName(),event.getLastName());
        PartyEntry.setType(event.getPartyType());
        PartyEntry.setName(event.getPartyName());
        PartyEntry.setState("initial");
        partyRepository.save(PartyEntry);
    }

    @Autowired
    public void setPartyRepository(PartyQueryRepository partyRepository) {
        this.partyRepository = partyRepository;
    }
}