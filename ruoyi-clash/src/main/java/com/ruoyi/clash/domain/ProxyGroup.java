package com.ruoyi.clash.domain;

import com.ruoyi.clash.annotation.validate.ConditionalNotBlank;
import com.ruoyi.clash.annotation.validate.ConditionalNotNull;
import com.ruoyi.clash.annotation.validate.ConditionalValid;
import com.ruoyi.clash.annotation.yaml.YamlProperty;
import com.ruoyi.clash.domain.base.ClashEntity;
import com.ruoyi.clash.enums.ProxyGroupType;
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
    @YamlProperty("interval")
    private Integer intervalSeconds;

    // 代理分组下属代理节点集合
    private List<ProxyNode> proxies;
}
