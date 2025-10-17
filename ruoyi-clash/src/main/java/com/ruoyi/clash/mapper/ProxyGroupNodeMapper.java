package com.ruoyi.clash.mapper;

import com.ruoyi.clash.domain.node.ProxyGroupNode;

import java.util.List;

public interface ProxyGroupNodeMapper {
    int batchInsert(List<ProxyGroupNode> itemList);

    int batchDelete(Long[] groupIds);
}
