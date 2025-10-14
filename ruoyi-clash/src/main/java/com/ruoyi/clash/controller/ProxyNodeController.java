package com.ruoyi.clash.controller;

import com.ruoyi.clash.dao.ProxyNodeRepository;
import com.ruoyi.clash.domain.node.ProxyNode;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("代理节点管理")
@RestController
@RequestMapping("/clash/proxyNode")
public class ProxyNodeController extends BaseController {
    @Autowired
    private ProxyNodeRepository repository;

    @ApiOperation("新增代理节点")
    @PostMapping()
    private AjaxResult save(@Validated @RequestBody ProxyNode proxyNode) {
        ProxyNode savedProxyNode = repository.save(proxyNode);
        return success(savedProxyNode);
    }
}
