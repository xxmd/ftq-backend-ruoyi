package com.ruoyi.clash.domain.node;

import com.ruoyi.clash.annotation.ConditionalNotBlank;
import com.ruoyi.clash.annotation.ConditionalNotNull;
import com.ruoyi.clash.annotation.ConditionalValid;
import com.ruoyi.clash.enums.CipherType;
import com.ruoyi.clash.enums.ProxyNodeType;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConditionalValid
@Data
public class ProxyNode extends BaseEntity {
    // 代理节点id
    private Long id;

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

    /**
     * 节点状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}
