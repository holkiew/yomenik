package com.holkiew.yomenik.battlesim.common;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public abstract class ReactorWorker {

    @PostConstruct
    public abstract void startWorker();
}
