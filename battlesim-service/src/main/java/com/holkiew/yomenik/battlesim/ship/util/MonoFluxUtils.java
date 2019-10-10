package com.holkiew.yomenik.battlesim.ship.util;

import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Function;

public class MonoFluxUtils {
    public static <T> Function<Planet, Publisher<? extends Tuple2<Planet, T>>> combineFluxWithMono(T object) {
        return planet -> Mono.just(object).map(fleet -> Tuples.of(planet, fleet));
    }

    public static <T> Function<Planet, Publisher<? extends Tuple2<Planet, T>>> combineFluxWithMono(Mono<T> mono) {
        return planet -> mono.map(fleet -> Tuples.of(planet, fleet));
    }
}
