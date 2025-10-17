package com.ruoyi.clash.enums;

/**
 * 分流策略类型
 */
public enum ProxyRulePolicy implements ReadAbleEnum<String> {
    DIRECT("DIRECT"), REJECT("REJECT"), PROXY("Proxy"), PROXY_GROUP("Proxy_Group");

    private String value;

    ProxyRulePolicy(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
