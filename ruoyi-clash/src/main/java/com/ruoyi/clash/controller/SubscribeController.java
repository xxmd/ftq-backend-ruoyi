package com.ruoyi.clash.controller;

import com.ruoyi.clash.domain.ClashConfig;
import com.ruoyi.clash.service.IProxyGroupService;
import com.ruoyi.clash.service.IProxyNodeService;
import com.ruoyi.clash.service.IProxyRuleService;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/clash/subscribe")
public class SubscribeController extends BaseController {
    @Autowired
    private IProxyNodeService nodeService;
    @Autowired
    private IProxyGroupService groupService;
    @Autowired
    private IProxyRuleService ruleService;

    @GetMapping(value = "", produces = "text/yaml; charset=UTF-8")
    public String get() {
        ClashConfig clashConfig = new ClashConfig();
        // TODO 添加缓存处理，无需每次查询数据库
        clashConfig.setProxies(nodeService.exactQuery(null));
        clashConfig.setProxyGroups(groupService.exactQuery(null));
        clashConfig.setRules(ruleService.exactQuery(null));
        Map<String, Object> yamlMap = clashConfig.toYamlMap(null);
        Yaml yaml = new Yaml();
        String output = yaml.dump(yamlMap);
        return output;
    }
}
