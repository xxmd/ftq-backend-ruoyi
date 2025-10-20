package com.ruoyi.clash.service.impl;

import com.ruoyi.clash.domain.ProxyGroup;
import com.ruoyi.clash.domain.ProxyGroupNode;
import com.ruoyi.clash.mapper.ProxyGroupMapper;
import com.ruoyi.clash.mapper.ProxyGroupNodeMapper;
import com.ruoyi.clash.service.IProxyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProxyGroupServiceImpl implements IProxyGroupService {
    @Autowired
    private ProxyGroupMapper mapper;
    @Autowired
    private ProxyGroupNodeMapper groupNodeMapper;

    @Override
    public List<ProxyGroup> fuzzyQuery(ProxyGroup proxyGroup) {
        return mapper.fuzzyQuery(proxyGroup);
    }

    @Override
    public List<ProxyGroup> exactQuery(ProxyGroup proxyGroup) {
        return mapper.exactQuery(proxyGroup);
    }

    @Override
    @Transient
    public int insert(ProxyGroup proxyGroup) {
        int rows = mapper.insert(proxyGroup);
        insertGroupNodeList(proxyGroup);
        return rows;
    }

    @Override
    public int update(ProxyGroup proxyGroup) {
        int rows = mapper.update(proxyGroup);
        groupNodeMapper.batchDelete(new Long[]{proxyGroup.getId()});
        insertGroupNodeList(proxyGroup);
        return rows;
    }

    private int insertGroupNodeList(ProxyGroup proxyGroup) {
        List<ProxyGroupNode> groupNodeList = proxyGroup.getProxies().stream().map(node -> {
            return new ProxyGroupNode(proxyGroup.getId(), node.getId());
        }).collect(Collectors.toList());
        return groupNodeMapper.batchInsert(groupNodeList);
    }

    @Override
    public int delete(Long[] ids) {
        int rows = mapper.delete(ids);
        groupNodeMapper.batchDelete(ids);
        return rows;
    }

    @Override
    public boolean isNameConflict(Long id, String name) {
        ProxyGroup templateItem = new ProxyGroup();
        templateItem.setName(name);
        List<ProxyGroup> itemList = mapper.exactQuery(templateItem);
        if (!itemList.isEmpty()) {
            return itemList.get(0).getId().equals(id);
        }
        return false;
    }
}
