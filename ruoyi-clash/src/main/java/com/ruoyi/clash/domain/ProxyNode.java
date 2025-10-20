package com.ruoyi.clash.domain;

import com.ruoyi.clash.annotation.validate.ConditionalNotBlank;
import com.ruoyi.clash.annotation.validate.ConditionalNotNull;
import com.ruoyi.clash.annotation.validate.ConditionalValid;
import com.ruoyi.clash.domain.base.ClashEntity;
import com.ruoyi.clash.enums.CipherType;
import com.ruoyi.clash.enums.ProxyNodeType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConditionalValid
@Data
public class ProxyNode extends ClashEntity {
    // 代理节点名称
    @NotBlank(message = "name节点名称不能为空")
    private String name;

    // 代理节点类型
    @NotNull(message = "type节点类型不能为空")
    private ProxyNodeType type;

    // 代理服务器地址
    @NotBlank(message = "server代理服务器地址不能为空")
    private String server;

    // 代理服务器端口
    @NotNull(message = "port代理服务器端口不能为空")
    private Integer port;

    // 开启tls
    @ConditionalNotNull(dependFieldName = "type", dependFieldValues = {"HTTP"})
    private Boolean tls;

    // 加密算法类型
    @ConditionalNotNull(dependFieldName = "type", dependFieldValues = {"SHADOW_SOCKS"})
    private CipherType cipher;

    // 代理认证密码
    @ConditionalNotBlank(dependFieldName = "type", dependFieldValues = {"SHADOW_SOCKS", "TROJAN"})
    private String password;

    // 允许udp流量
    private Boolean udp;

    // 服务器名称指示
    private String sni;

    // 跳过证书验证
    private Boolean skipCertVerify;

    @Override
    public Object toYamlMap(Object parent) {
        if (parent instanceof ProxyGroup) {
            return name;
        } else {
            return super.toYamlMap(parent);
        }
    }
}
