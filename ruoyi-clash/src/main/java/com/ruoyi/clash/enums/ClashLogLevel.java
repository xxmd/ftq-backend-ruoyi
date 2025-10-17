package com.ruoyi.clash.enums;

public enum ClashLogLevel implements ReadAbleEnum<String> {
    INFO("info"), DEBUG("debug");

    private String value;

    ClashLogLevel(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
