package com.ruoyi.clash.service;

import com.ruoyi.clash.domain.node.ProxyGroup;
import com.ruoyi.clash.domain.node.ProxyNode;

import java.util.List;

public interface IProxyGroupService {
    List<ProxyGroup> fuzzyQuery(ProxyGroup item);

    List<ProxyGroup> exactQuery(ProxyGroup item);

    int insert(ProxyGroup proxyGroup);

    int update(ProxyGroup proxyGroup);

    int delete(Long[] ids);

    boolean isNameConflict(Long id, String name);
}
