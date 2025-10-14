package com.ruoyi.clash.domain.node;

import com.ruoyi.clash.annotation.ConditionalNotBlank;
import com.ruoyi.clash.annotation.ConditionalNotNull;
import com.ruoyi.clash.annotation.ConditionalValid;
import com.ruoyi.clash.enums.CipherType;
import com.ruoyi.clash.enums.ProxyNodeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConditionalValid
@Data
@Entity
public class ProxyNode {
    @ApiModelProperty("代理节点id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(required = true, value = "代理节点名称", example = "默认名称")
    @NotBlank(message = "节点名称不能为空")
    private String name;

    @ApiModelProperty(required = true, value = "代理节点类型", example = "HTTP")
    @NotNull(message = "type节点类型不能为空")
    @Enumerated(EnumType.STRING)
    private ProxyNodeType type;

    @ApiModelProperty(required = true, value = "代理服务器地址", example = "proxy.server.com")
    @NotBlank(message = "server代理服务器地址不能为空")
    private String server;

    @ApiModelProperty(required = true, value = "代理服务器端口", example = "8080")
    @NotNull(message = "port代理服务器端口不能为空")
    private Integer port;

    @ApiModelProperty("是否开启tls")
    @ConditionalNotNull(dependFieldName = "type", dependFieldValues = {"HTTP"})
    private Boolean tls;

    @ApiModelProperty(value = "ShadowSocks加密算法", allowableValues = "AES_256_GCM")
    @ConditionalNotNull(dependFieldName = "type", dependFieldValues = {"SHADOW_SOCKS"})
    @Enumerated(EnumType.STRING)
    private CipherType cipher;

    @ApiModelProperty("代理认证密码")
    @ConditionalNotBlank(dependFieldName = "type", dependFieldValues = {"SHADOW_SOCKS", "TROJAN"})
    private String password;

    @ApiModelProperty("允许udp流量")
    private Boolean udp;

    @ApiModelProperty("服务器名称指示")
    private String sni;

    @ApiModelProperty("跳过证书验证")
    private Boolean skipCertVerify;
}
