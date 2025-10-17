package com.ruoyi.clash.domain;

import com.ruoyi.clash.domain.node.ProxyGroup;
import com.ruoyi.clash.domain.node.ProxyNode;
import com.ruoyi.clash.domain.node.ProxyRule;
import com.ruoyi.clash.enums.ClashLogLevel;
import com.ruoyi.clash.enums.ClashMode;
import lombok.Data;

import java.util.List;

@Data
public class ClashConfig extends ClashEntity {
    private Integer mixedPort = 7890;
    private Boolean allowLan = true;
    private String bindAddress = "*";
    private ClashMode mode = ClashMode.RULE;
    private ClashLogLevel level = ClashLogLevel.INFO;

    private List<ProxyNode> proxies;
    private List<ProxyGroup> proxyGroups;
    private List<ProxyRule> rules;
}


