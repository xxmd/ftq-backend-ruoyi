package com.ruoyi.clash.domain.node;

import com.ruoyi.clash.annotation.ConditionalNotNull;
import com.ruoyi.clash.annotation.ConditionalValid;
import com.ruoyi.clash.enums.ProxyRulePolicy;
import com.ruoyi.clash.enums.ProxyRuleType;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ConditionalValid
@Data
public class ProxyRule extends BaseEntity {
    // 分流规则id
    private Long id;

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

    private int sort;

    /**
     * 节点状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}
