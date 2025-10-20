package com.ruoyi.clash.domain;

import com.ruoyi.clash.annotation.validate.ConditionalNotNull;
import com.ruoyi.clash.annotation.validate.ConditionalValid;
import com.ruoyi.clash.domain.base.ClashEntity;
import com.ruoyi.clash.enums.ProxyRulePolicy;
import com.ruoyi.clash.enums.ProxyRuleType;
import com.ruoyi.common.utils.StringUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    @ConditionalNotNull(dependFieldName = "policy", dependFieldValues = {"PROXY"})
    private ProxyNode proxyNode;

    // PROXY_GROUP策略下对应代理分组
    @ConditionalNotNull(dependFieldName = "policy", dependFieldValues = {"PROXY_GROUP"})
    private ProxyGroup proxyGroup;

    // 排序字段
    private int sort;

    @Override
    public Object toYamlMap(Object parent) {
        List<String> parts = new ArrayList<>();
        parts.add(type.getValue());
        if (StringUtils.isNotEmpty(content)) {
            parts.add(content);
        }
        if (proxyGroup == null) {
            parts.add("DIRECT");
        } else {
            parts.add(proxyGroup.getName());
        }
        return String.join(",", parts);
    }
}
