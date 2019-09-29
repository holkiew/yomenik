package com.holkiew.yomenik.battlesim.ship.battlesimulator.model.exception;

public class HistoryAlreadyIssuedException extends RuntimeException {
    public HistoryAlreadyIssuedException(String message) {
        super(message);
    }
}
