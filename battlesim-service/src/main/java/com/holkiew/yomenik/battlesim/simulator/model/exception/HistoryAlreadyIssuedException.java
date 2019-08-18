package com.holkiew.yomenik.battlesim.simulator.model.exception;

public class HistoryAlreadyIssuedException extends RuntimeException {
    public HistoryAlreadyIssuedException(String message) {
        super(message);
    }
}
