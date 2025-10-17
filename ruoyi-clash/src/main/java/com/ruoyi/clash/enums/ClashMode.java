package com.ruoyi.clash.enums;

public enum ClashMode implements ReadAbleEnum<String> {
    RULE("rule"), GLOBAL("global");
    private String value;

    ClashMode(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
