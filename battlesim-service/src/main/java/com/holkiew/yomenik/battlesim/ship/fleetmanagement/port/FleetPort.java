package com.holkiew.yomenik.battlesim.ship.fleetmanagement.port;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import reactor.core.publisher.Flux;

public interface FleetPort {
    Flux<Fleet> findByIds(Iterable<String> ids);

    Flux<Fleet> findAllUnfinishedMissions(Principal id);
}
