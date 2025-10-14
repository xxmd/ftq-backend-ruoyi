create table proxy_node (
  id               bigint(20)                           not null auto_increment    comment '节点id',
  name             varchar(50)                          not null                   comment '节点名称',
  type             enum('HTTP','TROJAN','SHADOW_SOCKS') not null                   comment '节点类型',
  server           varchar(30)                          not null                   comment '代理服务器地址',
  port             int(4)                               not null                   comment '代理服务器端口',
  tls              tinyint(1)                                                      comment '是否开启tls[Http]',
  password         varchar(50)                                                     comment '认证密码[ShadowSocks, Trojan]',
  sni              varchar(50)                                                     comment '服务器名称指示[Trojan]',
  skip_cert_verify char(1)                                                         comment '跳过证书验证[Trojan]',
  udp              tinyint(1)                                                      comment '是否允许udp协议[ShadowSocks, Trojan]',
  cipher           enum('AES_256_GCM')                                             comment '加密算法[ShadowSocks]',
  create_by        varchar(64)                          default ''                 comment '创建者',
  create_time      datetime                                                        comment '创建时间',
  update_by        varchar(64)                          default ''                 comment '更新者',
  update_time      datetime                                                        comment '更新时间',
  primary key (id)
) engine=innodb comment='代理节点表';
