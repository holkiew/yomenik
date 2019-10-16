package com.holkiew.yomenik.battlesim.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;


@RequiredArgsConstructor
@Log4j2
@SuppressWarnings("unchecked")
public abstract class MongoInsertsLoader<D, T extends ReactiveMongoRepository<D, ?>> {

    @Getter
    protected final T repository;
    @Getter
    protected D[] data;
    @Setter
    protected Function<D, Mono<Boolean>> checkIfObjectAlreadyExistsFun;

    @Value("${initialInserts.clearAndInsert:false}")
    protected boolean clearAndInsert;

    public void insertData() {
        this.getDataStream().subscribe();
    }

    public Flux<D> getDataStream() {
        if (Objects.isNull(repository) || Objects.isNull(data) || Objects.isNull(checkIfObjectAlreadyExistsFun)) {
            log.error("Initial inserts, data unprepared, failed to begin insertion");
            return Flux.empty();
        }
        return clearAndInsert ? repository.deleteAll().thenMany(getInsertionUpstream()) : getInsertionUpstream();
    }

    private Flux<D> getInsertionUpstream() {
        AtomicInteger retryCounter = new AtomicInteger(1);
        return Flux.just(this.data)
                .flatMap(obj -> Mono.just(Tuples.of(obj, this.checkIfObjectAlreadyExistsFun.apply(obj))))
                .filterWhen(tuple -> BooleanUtils.not(tuple.getT2()))
                .flatMap(tuple -> repository.save(tuple.getT1()))
                .doOnNext(obj -> log.info("Saved object: " + obj))
                .doOnComplete(() ->
                        log.info("Initial injection of " + getClass().getSimpleName() + " completed"))
                .delaySubscription(Duration.ofSeconds(3))
                .doOnError(log::error)
                .retryWhen(Retry.any()
                        .fixedBackoff(Duration.ofSeconds(10))
                        .doOnRetry(context -> log.warn("Error, retry " + retryCounter.addAndGet(1))));
    }

    public void setData(D... data) {
        this.data = data;
    }
}
