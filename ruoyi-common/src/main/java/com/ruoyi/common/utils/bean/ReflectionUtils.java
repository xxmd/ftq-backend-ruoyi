package com.ruoyi.common.utils.bean;

import java.lang.reflect.*;
import java.util.List;

public class ReflectionUtils {

    /**
     * 判断字段是否是 type 类型 或 List<type> 类型
     *
     * @param field 要检查的字段
     * @param type  目标类型
     * @return true 如果字段是 type 或 List<type>
     */
    public static boolean isFieldOrListOfType(Field field, Class<?> type) {
        Class<?> fieldClass = field.getType();

        // 1️⃣ 字段是目标类型本身
        if (type.isAssignableFrom(fieldClass)) {
            return true;
        }

        // 2️⃣ 字段是 List 类型
        if (List.class.isAssignableFrom(fieldClass)) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType pt) {
                Type actualType = pt.getActualTypeArguments()[0];
                if (actualType instanceof Class<?> clazz) {
                    return type.isAssignableFrom(clazz);
                }
            }
        }

        return false;
    }
}
