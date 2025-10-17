package com.ruoyi.clash.enums;

public enum ProxyNodeType implements ReadAbleEnum<String>{
    HTTP("http"), TROJAN("trojan"), SHADOW_SOCKS("ss");
    private String value;

    ProxyNodeType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
