package com.ruoyi.clash.controller;

import com.ruoyi.clash.domain.ProxyRule;
import com.ruoyi.clash.service.IProxyRuleService;
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
@RequestMapping("/clash/proxyRule")
public class ProxyRuleController extends BaseController {
    @Autowired
    private IProxyRuleService service;

    @PreAuthorize("@ss.hasPermi('clash:rule:list')")
    @GetMapping("/fuzzyQuery")
    public TableDataInfo fuzzyQuery(ProxyRule proxyRule) {
        startPage();
        List<ProxyRule> list = service.fuzzyQuery(proxyRule);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('clash:rule:list')")
    @PostMapping("/exactQuery")
    public AjaxResult exactQuery(@RequestBody ProxyRule proxyRule) {
        return AjaxResult.success(service.exactQuery(proxyRule));
    }

    @PreAuthorize("@ss.hasPermi('clash:rule:list')")
    @GetMapping("/{id}")
    public AjaxResult getItem(@PathVariable("id") Long id) {
        ProxyRule templateItem = new ProxyRule();
        templateItem.setId(id);
        List<ProxyRule> itemList = service.exactQuery(templateItem);
        ProxyRule daoItem = itemList.isEmpty() ? null : itemList.get(0);
        return AjaxResult.success(daoItem);
    }

    @PreAuthorize("@ss.hasPermi('clash:rule:add')")
    @Log(title = "分流规则管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ProxyRule item) {
        item.setCreateBy(getUsername());
        return toAjax(service.insert(item));
    }

    @PreAuthorize("@ss.hasPermi('clash:rule:edit')")
    @Log(title = "分流规则管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ProxyRule item) {
        item.setUpdateBy(getUsername());
        return toAjax(service.update(item));
    }

    @PreAuthorize("@ss.hasPermi('clash:rule:remove')")
    @Log(title = "分流规则管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.delete(ids));
    }
}
