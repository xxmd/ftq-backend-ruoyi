package com.ruoyi.clash.controller;

import com.ruoyi.clash.domain.node.ProxyGroup;
import com.ruoyi.clash.service.IProxyGroupService;
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
@RequestMapping("/clash/proxyGroup")
public class ProxyGroupController extends BaseController {
    @Autowired
    private IProxyGroupService service;

    @PreAuthorize("@ss.hasPermi('clash:group:list')")
    @GetMapping("/fuzzyQuery")
    public TableDataInfo fuzzyQuery(ProxyGroup proxyGroup) {
        startPage();
        List<ProxyGroup> list = service.fuzzyQuery(proxyGroup);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('clash:node:list')")
    @PostMapping("/exactQuery")
    public AjaxResult exactQuery(@RequestBody ProxyGroup item) {
        return AjaxResult.success(service.exactQuery(item));
    }

    @PreAuthorize("@ss.hasPermi('clash:group:list')")
    @GetMapping("/{id}")
    public AjaxResult getItem(@PathVariable("id") Long id) {
        ProxyGroup templateItem = new ProxyGroup();
        templateItem.setId(id);
        List<ProxyGroup> itemList = service.exactQuery(templateItem);
        ProxyGroup daoItem = itemList.isEmpty() ? null : itemList.get(0);
        return AjaxResult.success(daoItem);
    }

    @PreAuthorize("@ss.hasPermi('clash:group:add')")
    @Log(title = "代理分组管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ProxyGroup item) {
        if (service.isNameConflict(item.getId(), item.getName())) {
            return error("新增代理分组'" + item.getName() + "'失败，该代理分组名称已存在");
        }
        item.setCreateBy(getUsername());
        return toAjax(service.insert(item));
    }

    @PreAuthorize("@ss.hasPermi('clash:node:edit')")
    @Log(title = "代理分组管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ProxyGroup item) {
        if (!service.isNameConflict(item.getId(), item.getName())) {
            return error("更新代理分组'" + item.getName() + "'失败，该代理分组名称已存在");
        }
        item.setUpdateBy(getUsername());
        return toAjax(service.update(item));
    }

    @PreAuthorize("@ss.hasPermi('clash:node:remove')")
    @Log(title = "代理分组管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.delete(ids));
    }
}
