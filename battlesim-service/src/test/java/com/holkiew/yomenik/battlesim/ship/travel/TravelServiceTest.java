package com.holkiew.yomenik.battlesim.ship.travel;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class TravelServiceTest {


    @Test
    public void dupa() {
        Stream.generate(() -> LocalDateTime.now().plusSeconds((long) (Math.random() * 10))).limit(20);
        Flux.interval(Duration.ofSeconds(1L))
                .flatMap(aLong -> Flux.just("a1", "a2"))
                .doOnEach(System.out::println)
                .subscribe();

    }


}