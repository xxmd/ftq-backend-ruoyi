package com.ruoyi.clash.mapper;

import com.ruoyi.clash.domain.node.ProxyNode;
import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.List;

public interface ProxyNodeMapper {
    List<ProxyNode> selectNodeList(ProxyNode proxyNode);

    int insertNode(ProxyNode proxyNode);

    int updateNode(ProxyNode proxyNode);

    int deleteNodeByIds(Long[] ids);

    ProxyNode checkNodeNameUnique(String name);

    List<ProxyNode> exactQuery(ProxyNode proxyNode);
}
