package com.lg.annotation;

import com.lg.validator.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 自定义校验Validation中的校验注解
 * 是否是手机号格式
 *
 * @author wb-xlg283120
 * @version $Id: CheckEnum.java, v 0.1 2017/11/27 15:41 wb-xlg283120 Exp $
 */
@Documented
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Constraint(validatedBy = {MobileValidator.class})
public @interface Mobile {

    String message() default "invalid mobile value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否使用严格的校验手机号模式
     *
     * @return true if strict
     */
    boolean isStrict() default true;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Mobile[] value();
    }
}
