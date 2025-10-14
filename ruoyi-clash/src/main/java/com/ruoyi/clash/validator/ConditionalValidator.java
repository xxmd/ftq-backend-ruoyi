package com.ruoyi.clash.validator;

import com.ruoyi.clash.annotation.ConditionalNotBlank;
import com.ruoyi.clash.annotation.ConditionalNotNull;
import com.ruoyi.clash.annotation.ConditionalValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ConditionalValidator implements ConstraintValidator<ConditionalValid, Object> {

    private static final Logger log = LoggerFactory.getLogger(ConditionalValidator.class);

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) return true;
        boolean valid = true;
        Field[] fields = obj.getClass().getDeclaredFields();
        // 允许记录多个字段错误
        context.disableDefaultConstraintViolation();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                ConditionalNotNull notNull = field.getAnnotation(ConditionalNotNull.class);
                if (notNull != null) {
                    valid = checkCondition(obj, field, notNull, ConditionAnnoHandlerFactory.get(notNull), context);
                }
                ConditionalNotBlank notBlank = field.getAnnotation(ConditionalNotBlank.class);
                if (notBlank != null) {
                    valid = checkCondition(obj, field, notBlank, ConditionAnnoHandlerFactory.get(notBlank), context);
                }
            } catch (Exception e) {
                log.error("参数条件校验异常", e);
            }
        }
        return valid;
    }

    private boolean checkCondition(Object obj, Field targetField, Annotation annotation, ConditionAnnoHandler conditionAnnoHandler, ConstraintValidatorContext context)
            throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        // 1. 获取依赖字段字符串格式值
        Method dfnMethod = annotation.getClass().getDeclaredMethod("dependFieldName");
        String dependFieldName = (String) dfnMethod.invoke(annotation);
        Field dependsField = obj.getClass().getDeclaredField(dependFieldName);
        dependsField.setAccessible(true);
        Object dependFiledValue = dependsField.get(obj);
        if (dependFiledValue == null) {
            return true;
        }
        String dependFieldStrValue = dependFiledValue.getClass().isEnum() ? ((Enum<?>) dependFiledValue).name() : dependFiledValue.toString();

        // 2. 获取注解标记的依赖字段允许值
        Method dfvMethod = annotation.getClass().getDeclaredMethod("dependFieldValues");
        String[] annoAllowValues = (String[]) dfvMethod.invoke(annotation);

        // 3. 检查是否符合校验前提（允许字段值包含实际值）
        if (isMeetCheckCriteria(annoAllowValues, dependFieldStrValue)) {
            targetField.setAccessible(true);
            Object targetFiledValue = targetField.get(obj);
            boolean valid = conditionAnnoHandler.isValid(targetFiledValue);
            if (!valid) {
                String tips = conditionAnnoHandler.getTipsOnInValid(targetField, dependsField, dependFieldStrValue);
                context.buildConstraintViolationWithTemplate(tips)
                        .addPropertyNode(targetField.getName())
                        .addConstraintViolation();
            }
            return valid;
        }
        return true;
    }

    /**
     * 检查是否满足校验前提
     *
     * @param annoAllowValues     注解标记允许的值
     * @param dependFieldStrValue 依赖字段字符串格式值
     * @return 是否满足校验前提
     */
    private boolean isMeetCheckCriteria(String[] annoAllowValues, String dependFieldStrValue) {
        return Arrays.stream(annoAllowValues)
                .anyMatch(s -> s.equalsIgnoreCase(dependFieldStrValue));
    }
}

