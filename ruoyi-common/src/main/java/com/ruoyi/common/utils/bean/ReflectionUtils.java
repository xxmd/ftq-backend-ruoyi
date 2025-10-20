package com.ruoyi.common.utils.bean;

import java.lang.reflect.*;
import java.util.List;

public class ReflectionUtils {

    /**
     * 判断字段是否是 List<type> 类型
     *
     * @param field 要检查的字段
     * @param type  目标类型
     * @return true 如果字段是 List<type>
     */
    public static boolean isListOfType(Field field, Class<?> type) {
        // 先判断是不是 List 或其子类
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            // 不是带泛型参数的 List，例如原生类型 List
            return false;
        }

        // 获取泛型参数
        ParameterizedType pt = (ParameterizedType) genericType;
        Type[] actualTypes = pt.getActualTypeArguments();
        if (actualTypes.length != 1) {
            return false;
        }

        Type actualType = actualTypes[0];

        // 如果泛型参数是 Class<?> 类型，直接比较
        if (actualType instanceof Class<?>) {
            Class<?> actualClass = (Class<?>) actualType;
            return type.isAssignableFrom(actualClass);
        }

        // 如果是通配符类型，比如 List<? extends ClashEntity>
        if (actualType instanceof WildcardType) {
            WildcardType wt = (WildcardType) actualType;
            Type[] upperBounds = wt.getUpperBounds();
            if (upperBounds.length > 0 && upperBounds[0] instanceof Class<?>) {
                return type.isAssignableFrom((Class<?>) upperBounds[0]);
            }
        }

        // 其他复杂泛型情况（比如 List<T>，T 是类型变量）
        if (actualType instanceof TypeVariable) {
            // 无法在运行时确定具体类型，只能返回 true（保守假设）
            return true;
        }

        return false;
    }

}
