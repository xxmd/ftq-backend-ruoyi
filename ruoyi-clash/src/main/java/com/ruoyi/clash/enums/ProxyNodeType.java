package com.ruoyi.clash.enums;

public enum ProxyNodeType {
    HTTP("http"), TROJAN("trojan"), SHADOW_SOCKS("ss");
    private String value;

    ProxyNodeType(String value) {
        this.value = value;
    }
}
