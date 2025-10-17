package com.ruoyi.clash.service.impl;

import com.ruoyi.clash.domain.node.ProxyNode;
import com.ruoyi.clash.mapper.ProxyNodeMapper;
import com.ruoyi.clash.service.IProxyNodeService;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProxyNodeServiceImpl implements IProxyNodeService {
    @Autowired
    private ProxyNodeMapper proxyNodeMapper;

    @Override
    public List<ProxyNode> selectNodeList(ProxyNode proxyNode) {
        return proxyNodeMapper.selectNodeList(proxyNode);
    }

    @Override
    public int insertNode(ProxyNode proxyNode) {
        return proxyNodeMapper.insertNode(proxyNode);
    }

    @Override
    public int updateNode(ProxyNode proxyNode) {
        return proxyNodeMapper.updateNode(proxyNode);
    }

    @Override
    public int deleteNodeByIds(Long[] ids) {
        return proxyNodeMapper.deleteNodeByIds(ids);
    }

    @Override
    public boolean checkNodeNameUnique(ProxyNode proxyNode) {
        Long id = StringUtils.isNull(proxyNode.getId()) ? -1L : proxyNode.getId();
        ProxyNode daoProxyNode = proxyNodeMapper.checkNodeNameUnique(proxyNode.getName());
        if (StringUtils.isNotNull(daoProxyNode) && daoProxyNode.getId().longValue() != id.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public List<ProxyNode> exactQuery(ProxyNode proxyNode) {
        return proxyNodeMapper.exactQuery(proxyNode);
    }
}
