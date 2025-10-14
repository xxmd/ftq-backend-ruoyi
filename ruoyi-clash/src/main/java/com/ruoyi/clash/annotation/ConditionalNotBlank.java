package com.ruoyi.clash.annotation;

import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.function.Function;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConditionalNotBlank {

    String message() default "{field}不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 依赖的字段名
     */
    String dependFieldName();

    /**
     * 依赖字段的值（满足这个条件才触发）
     */
    String[] dependFieldValues();
}
