package com.holkiew.yomenik.battlesim.configuration.webflux;

import com.holkiew.yomenik.battlesim.common.ReactorWorker;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Log4j2
@Configuration
public class ReactorWorkerConfig {

    private final Map<String, ReactorWorker> workers;

    public ReactorWorkerConfig(Map<String, ReactorWorker> workers) {
        this.workers = workers;
        this.runWorkers();
    }

    public void runWorkers() {
        Scheduler worker = Schedulers.parallel();
        this.workers.forEach((qualifier, reactorWorker) ->
                        reactorWorker.getUpstream()
//                        .subscribeOn(worker)
                                .doOnError(reactorWorker.doOnError())
                                .doOnSubscribe(reactorWorker.doOnSubscribe())
                                .doOnTerminate(reactorWorker.doOnTerminate())
                                .subscribe()
        );
        log.info("Workers subscribed");
    }
}
