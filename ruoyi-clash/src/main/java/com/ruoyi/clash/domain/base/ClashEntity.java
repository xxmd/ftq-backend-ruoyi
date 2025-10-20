package com.ruoyi.clash.domain.base;

import com.ruoyi.clash.annotation.yaml.YamlIgnore;
import com.ruoyi.clash.annotation.yaml.YamlProperty;
import com.ruoyi.clash.enums.ReadAbleEnum;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.bean.ReflectionUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ClashEntity extends BaseEntity {
    private static final Logger log = LoggerFactory.getLogger(ClashEntity.class);

    private Long id;

    /**
     * 节点状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public Object toYamlMap(Object parent) {
        Field[] fields = getClass().getDeclaredFields();
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                YamlIgnore yamlIgnore = field.getAnnotation(YamlIgnore.class);
                if (yamlIgnore != null) {
                    continue;
                }
                Object value = field.get(this);
                if (value == null) {
                    continue;
                }
                if (value instanceof ReadAbleEnum) {
                    value = ((ReadAbleEnum) value).getValue().toString();
                } else if (value instanceof Integer || value instanceof Boolean) {

                } else if (ReflectionUtils.isListOfType(field, ClashEntity.class)) {
                    List listValue = (List) value;
                    value = listValue.stream().map(it -> {
                        ClashEntity childEntity = (ClashEntity) it;
                        return childEntity.toYamlMap(this);
                    }).collect(Collectors.toList());
                } else {
                    value = value.toString();
                }
                map.put(convertYamlFieldName(field), value);
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return map;
    }

    private String convertYamlFieldName(Field field) {
        YamlProperty yamlProperty = field.getAnnotation(YamlProperty.class);
        if (yamlProperty != null && StringUtils.isNotEmpty(yamlProperty.value())) {
            return yamlProperty.value();
        }
        String name = field.getName();
        if (name == null || name.isEmpty()) {
            return name;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('-');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
