package com.ruoyi.clash.service;

import com.ruoyi.clash.domain.node.ProxyRule;

import java.util.List;

public interface IProxyRuleService {
    List<ProxyRule> fuzzyQuery(ProxyRule item);

    List<ProxyRule> exactQuery(ProxyRule item);

    int insert(ProxyRule proxyRule);

    int update(ProxyRule proxyRule);

    int delete(Long[] ids);
}
