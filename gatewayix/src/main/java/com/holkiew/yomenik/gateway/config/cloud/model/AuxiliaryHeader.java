package com.holkiew.yomenik.gateway.config.cloud.model;

public enum AuxiliaryHeader {
    PRINCIPAL_NAME("PRINCIPAL-NAME"), PRINCIPAL_ID("PRINCIPAL-ID");

    public String name;

    AuxiliaryHeader(String name) {
        this.name = name;
    }
}
