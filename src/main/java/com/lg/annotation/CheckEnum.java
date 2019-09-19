package com.lg.annotation;

import com.lg.validator.CheckEnumValidator;

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
 * 检查值是否在枚举中
 * 要求枚举有getCode()方法来获取枚举值
 *
 * @author wb-xlg283120
 * @version $Id: CheckEnum.java, v 0.1 2017/11/27 15:41 wb-xlg283120 Exp $
 */
@Documented
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Constraint(validatedBy = {CheckEnumValidator.class})
public @interface CheckEnum {

    String GET_BY_CODE = "getCode";

    String GET_BY_NAME = "name";

    String GET_BY_VALUE_OF = "valueOf";

    String message() default "invalid enum value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 检查的枚举的Class类型
     *
     * @return null
     */
    Class<? extends Enum> value();

    /**
     * 白名单列表
     * 表示哪些枚举值需要做校验
     * 默认是所有的枚举值都参与校验
     *
     * @return the white list
     */
    String[] whitelist() default {};

    /**
     * 获取枚举值的方法
     * 默认使用getCode()方法获取枚举的值
     *
     * @return the method name of get the enum value
     */
    String getter() default GET_BY_CODE;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        CheckEnum[] value();
    }

}
