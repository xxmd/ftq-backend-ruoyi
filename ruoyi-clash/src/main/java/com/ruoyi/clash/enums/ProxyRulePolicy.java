package com.ruoyi.clash.enums;

/**
 * 分流策略类型
 */
public enum ProxyRulePolicy {
    DIRECT("DIRECT"), REJECT("REJECT"), PROXY("Proxy"), PROXY_GROUP("Proxy_Group");

    private String value;

    ProxyRulePolicy(String value) {
        this.value = value;
    }
}
