package com.holkiew.yomenik.battlesim.planet.port;

import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import reactor.core.publisher.Flux;


public interface FleetPort {
    Flux<Fleet> findByIds(Iterable<String> ids);
}
