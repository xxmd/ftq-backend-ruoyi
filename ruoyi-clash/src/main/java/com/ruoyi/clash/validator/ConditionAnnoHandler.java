package com.ruoyi.clash.validator;

import java.lang.reflect.Field;

/**
 * 条件注解处理器
 */
public interface ConditionAnnoHandler {
    /**
     * 判断目标字段值是否合法
     *
     * @param targetFiledValue 目标字段值
     * @return 目标字段值是否合法
     */
    Boolean isValid(Object targetFiledValue);

    /**
     * 当字段值不合法时获取提示
     *
     * @param targetField      目标字段
     * @param dependFiled      依赖的字段
     * @param dependFiledValue 依赖的字段值
     * @return 提示内容
     */
    String getTipsOnInValid(Field targetField, Field dependFiled, String dependFiledValue);
}