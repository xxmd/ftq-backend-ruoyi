package com.ruoyi.clash.domain.node;

import com.ruoyi.clash.annotation.ConditionalNotNull;
import com.ruoyi.clash.annotation.ConditionalValid;
import com.ruoyi.clash.annotation.YamlIgnore;
import com.ruoyi.clash.domain.ClashEntity;
import com.ruoyi.clash.enums.ProxyRulePolicy;
import com.ruoyi.clash.enums.ProxyRuleType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ConditionalValid
@Data
public class ProxyRule extends ClashEntity {
    // 分流规则类型
    @NotNull(message = "分流规则类型不能为空")
    private ProxyRuleType type;

    // 分流规则内容
    private String content;

    // 分流策略
    private ProxyRulePolicy policy;

    // PROXY策略下对应代理节点
    @YamlIgnore
    @ConditionalNotNull(dependFieldName = "policy", dependFieldValues = {"PROXY"})
    private ProxyNode proxyNode;

    // PROXY_GROUP策略下对应代理分组
    @YamlIgnore
    @ConditionalNotNull(dependFieldName = "policy", dependFieldValues = {"PROXY_GROUP"})
    private ProxyGroup proxyGroup;

    // 排序字段
    @YamlIgnore
    private int sort;
}
