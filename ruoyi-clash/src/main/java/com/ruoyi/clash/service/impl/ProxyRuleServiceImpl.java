package com.ruoyi.clash.service.impl;

import com.ruoyi.clash.domain.node.ProxyGroup;
import com.ruoyi.clash.domain.node.ProxyGroupNode;
import com.ruoyi.clash.domain.node.ProxyRule;
import com.ruoyi.clash.enums.ProxyRulePolicy;
import com.ruoyi.clash.mapper.ProxyRuleMapper;
import com.ruoyi.clash.service.IProxyRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProxyRuleServiceImpl implements IProxyRuleService {
    @Autowired
    private ProxyRuleMapper mapper;

    @Override
    public List<ProxyRule> fuzzyQuery(ProxyRule proxyRule) {
        return mapper.fuzzyQuery(proxyRule);
    }

    @Override
    public List<ProxyRule> exactQuery(ProxyRule proxyRule) {
        return mapper.exactQuery(proxyRule);
    }

    @Override
    @Transient
    public int insert(ProxyRule proxyRule) {
        checkPolicyType(proxyRule);
        return mapper.insert(proxyRule);
    }

    @Override
    public int update(ProxyRule proxyRule) {
        checkPolicyType(proxyRule);
        return mapper.update(proxyRule);
    }

    private void checkPolicyType(ProxyRule proxyRule) {
        if (ProxyRulePolicy.DIRECT.equals(proxyRule.getPolicy())) {
            proxyRule.setProxyNode(null);
            proxyRule.setProxyGroup(null);
        }
    }

    @Override
    public int delete(Long[] ids) {
        return mapper.delete(ids);
    }
}
