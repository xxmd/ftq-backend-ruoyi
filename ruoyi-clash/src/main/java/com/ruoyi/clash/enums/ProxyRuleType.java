package com.ruoyi.clash.enums;

/**
 * 分流规则类型
 */
public enum ProxyRuleType {
    GEOIP("GEOIP"), DOMAIN("DOMAIN"), DOMAIN_SUFFIX("DOMAIN-SUFFIX"), DOMAIN_KEYWORD("DOMAIN-KEYWORD"), MATCH("MATCH");

    private String value;

    ProxyRuleType(String value) {
        this.value = value;
    }
}
