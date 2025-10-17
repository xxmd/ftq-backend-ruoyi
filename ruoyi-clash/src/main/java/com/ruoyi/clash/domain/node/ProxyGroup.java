package com.ruoyi.clash.domain.node;

import com.ruoyi.clash.annotation.ConditionalNotBlank;
import com.ruoyi.clash.annotation.ConditionalNotNull;
import com.ruoyi.clash.annotation.ConditionalValid;
import com.ruoyi.clash.annotation.YamlIgnore;
import com.ruoyi.clash.domain.ClashEntity;
import com.ruoyi.clash.enums.ProxyGroupType;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ConditionalValid
@Data
public class ProxyGroup extends ClashEntity {
    // 代理分组名称
    @NotBlank(message = "代理分组名称不能为空")
    private String name;

    // 代理分组类型
    @NotNull(message = "代理分组类型不能为空")
    private ProxyGroupType type;

    // 代理分组延迟测试链接
    @ConditionalNotBlank(dependFieldName = "type", dependFieldValues = {"URL_TEST"})
    private String url;

    // 代理分组延迟测试间隔
    @ConditionalNotNull(dependFieldName = "type", dependFieldValues = {"URL_TEST"})
    private Integer intervalSeconds;

    // 代理分组下属代理节点集合
    @YamlIgnore
    private List<ProxyNode> proxies;
}
