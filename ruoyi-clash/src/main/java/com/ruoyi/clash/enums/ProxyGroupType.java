package com.ruoyi.clash.enums;

/**
 * 代理分组类型
 */
public enum ProxyGroupType {
    URL_TEST("url-test"), MANUAL_SELECT("select"), LOAD_BALANCE("load-balance");
    private String value;

    ProxyGroupType(String value) {
        this.value = value;
    }
}
