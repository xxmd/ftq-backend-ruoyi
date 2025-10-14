package com.ruoyi.clash.annotation;

import com.ruoyi.clash.validator.ConditionalValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalValidator.class)
@Documented
public @interface ConditionalValid {

    String message() default "条件校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
