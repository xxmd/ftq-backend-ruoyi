package com.ruoyi.clash.controller;

import com.ruoyi.clash.domain.node.ProxyNode;
import com.ruoyi.clash.service.IProxyNodeService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clash/proxyNode")
public class ProxyNodeController extends BaseController {
    @Autowired
    private IProxyNodeService proxyNodeService;

    @PreAuthorize("@ss.hasPermi('clash:node:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProxyNode proxyNode) {
        startPage();
        List<ProxyNode> list = proxyNodeService.selectNodeList(proxyNode);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('clash:node:list')")
    @GetMapping("/{id}")
    public AjaxResult getItem(@PathVariable("id") Long id) {
        ProxyNode templateNode = new ProxyNode();
        templateNode.setId(id);
        List<ProxyNode> nodeList = proxyNodeService.exactQuery(templateNode);
        ProxyNode daoProxyNode = nodeList.isEmpty() ? null : nodeList.get(0);
        return AjaxResult.success(daoProxyNode);
    }

    /**
     * 新增代理节点
     */
    @PreAuthorize("@ss.hasPermi('clash:node:add')")
    @Log(title = "节点管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ProxyNode proxyNode) {
        if (!proxyNodeService.checkNodeNameUnique(proxyNode)) {
            return error("新增节点'" + proxyNode.getName() + "'失败，节点名称已存在");
        }
        proxyNode.setCreateBy(getUsername());
        return toAjax(proxyNodeService.insertNode(proxyNode));
    }

    /**
     * 修改代理节点
     */
    @PreAuthorize("@ss.hasPermi('clash:node:edit')")
    @Log(title = "节点管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ProxyNode proxyNode) {
        if (!proxyNodeService.checkNodeNameUnique(proxyNode)) {
            return error("更新节点'" + proxyNode.getName() + "'失败，节点名称已存在");
        }
        proxyNode.setUpdateBy(getUsername());
        return toAjax(proxyNodeService.updateNode(proxyNode));
    }

    /**
     * 删除代理节点
     */
    @PreAuthorize("@ss.hasPermi('clash:node:remove')")
    @Log(title = "节点管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(proxyNodeService.deleteNodeByIds(ids));
    }

    /**
     * 检查代理节点唯一性
     */
    @PreAuthorize("@ss.hasPermi('clash:node:list')")
    @PostMapping("/exactQuery")
    public AjaxResult exactQuery(@RequestBody ProxyNode proxyNode) {
        return AjaxResult.success(proxyNodeService.exactQuery(proxyNode));
    }
}
