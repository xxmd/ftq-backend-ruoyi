package com.ruoyi.clash.mapper;

import com.ruoyi.clash.domain.ProxyRule;

import java.util.List;

public interface ProxyRuleMapper {
    List<ProxyRule> fuzzyQuery(ProxyRule proxyRule);

    List<ProxyRule> exactQuery(ProxyRule proxyRule);

    int insert(ProxyRule proxyRule);

    int update(ProxyRule proxyRule);

    int delete(Long[] ids);
}
