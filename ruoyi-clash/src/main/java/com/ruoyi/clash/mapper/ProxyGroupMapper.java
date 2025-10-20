package com.ruoyi.clash.mapper;

import com.ruoyi.clash.domain.ProxyGroup;

import java.util.List;

public interface ProxyGroupMapper {
    List<ProxyGroup> fuzzyQuery(ProxyGroup proxyGroup);

    List<ProxyGroup> exactQuery(ProxyGroup proxyGroup);

    int insert(ProxyGroup proxyGroup);

    int update(ProxyGroup proxyGroup);

    int delete(Long[] ids);
}
