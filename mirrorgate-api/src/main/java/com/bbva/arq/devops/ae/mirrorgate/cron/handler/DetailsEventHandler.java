/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
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

package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "DashboardType")
public class DetailsEventHandler implements EventHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(DetailsEventHandler.class);

    private DashboardService dashboardService;
    private ProcessEventsHelper eventsHelper;

    @Autowired
    public DetailsEventHandler(DashboardService dashboardService,
            ProcessEventsHelper eventsHelper){

        this.dashboardService = dashboardService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<ObjectId> idList = eventList.stream()
                .map(Event::getEventTypeCollectionId)
                .collect(Collectors.toList());

        Predicate<Dashboard> filterDashboards = d -> idList.contains(d.getId());

        eventsHelper.processEvents(dashboardIds, filterDashboards, EventType.DETAIL);
    }

}
