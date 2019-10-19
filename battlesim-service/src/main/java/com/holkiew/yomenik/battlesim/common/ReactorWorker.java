package com.holkiew.yomenik.battlesim.common;

import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

public interface ReactorWorker {
    Flux<?> getUpstream();

    Consumer<Throwable> doOnError();

    Runnable doOnTerminate();

    Consumer<Subscription> doOnSubscribe();
}
