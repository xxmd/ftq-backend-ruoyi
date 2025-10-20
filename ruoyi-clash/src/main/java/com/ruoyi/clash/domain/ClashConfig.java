package com.ruoyi.clash.domain;

import com.ruoyi.clash.domain.base.ClashEntity;
import com.ruoyi.clash.enums.ClashLogLevel;
import com.ruoyi.clash.enums.ClashMode;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> toYamlMap(Object parent) {
        return (Map<String, Object>) super.toYamlMap(parent);
    }
}


