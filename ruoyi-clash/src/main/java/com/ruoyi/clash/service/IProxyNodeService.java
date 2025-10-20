package com.ruoyi.clash.service;

import com.ruoyi.clash.domain.ProxyNode;

import java.util.List;

public interface IProxyNodeService {
    List<ProxyNode> selectNodeList(ProxyNode proxyNode);

    int insertNode(ProxyNode proxyNode);

    int updateNode(ProxyNode proxyNode);

    int deleteNodeByIds(Long[] ids);

    boolean checkNodeNameUnique(ProxyNode proxyNode);

    List<ProxyNode> exactQuery(ProxyNode proxyNode);

    List<ProxyNode> getAllActiveNode();
}
