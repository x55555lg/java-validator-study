package com.lg.validator;

import com.lg.annotation.CheckEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自定义枚举值的校验器
 *
 * @author wb-xlg283120
 * @version $Id: CheckEnumValidator.java, v 0.1 2017/11/27 15:38 wb-xlg283120 Exp $
 */
public class CheckEnumValidator implements ConstraintValidator<CheckEnum, Object> {

    /**
     * 枚举的类型
     */
    private Class<? extends Enum> enumClass;

    /**
     * 枚举的所有值的映射
     */
    private Map<String, Enum> enumValues = new HashMap<>();

    /**
     * 白名单，有效的枚举值约束
     */
    private Set<Enum> whitelistEnumValues = new HashSet<>();

    @Override
    public void initialize(CheckEnum annotation) {
        // 获取被校验的枚举类型
        Class<? extends Enum> enumClass = annotation.value();
        // 枚举获取值的方法名称
        String methodName = annotation.getter();
        // 白名单
        String[] whitelist = annotation.whitelist();
        this.initEnumValues(enumClass, methodName, whitelist);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 必须先这么写...
        if (value == null) {
            return true;
        }
        Enum anEnum = this.parseEnum(value);
        // 当解析出的枚举对象存在并且也在白名单重时，返回true
        return anEnum != null && this.whitelistEnumValues.contains(anEnum);
    }

    /**
     * 校验器初始化
     *
     * @param enumClass  枚举类型
     * @param methodName 获取枚举值的方法名称
     * @param whitelist  白名单
     */
    private void initEnumValues(Class<? extends Enum> enumClass,
                                String methodName, String[] whitelist) {
        this.enumClass = enumClass;
        if (methodName == null || methodName.isEmpty()) {
            throw new IllegalArgumentException("CheckEnum Constraint: "
                    + "getter is override by empty method name.");
        }
        Enum[] enums = enumClass.getEnumConstants();
        try {
            // 获取枚举的获取code的方法
            Method method = enumClass.getMethod(methodName);
            // 枚举类型的所有值建立索引(K: code, V: enum)
            for (Enum enumValue : enums) {
                String enumCode = (String) method.invoke(enumValue);
                this.enumValues.put(enumCode, enumValue);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
        // 设置白名单
        if (whitelist.length > 0) {
            for (String code : whitelist) {
                Enum anEnum = this.parseEnum(code);
                if (anEnum == null) {
                    throw new IllegalArgumentException("Enum Value: "
                            + code + " is not exist for the " + enumClass);
                }
                this.whitelistEnumValues.add(anEnum);
            }
        } else {
            // ~~没有指定白名单，则所有枚举都要添加到白名单范围内
            Collections.addAll(this.whitelistEnumValues, enums);
        }
    }

    /**
     * 根据枚举值解析成枚举
     *
     * @param code 枚举值
     * @return 枚举对象
     */
    private Enum parseEnum(Object code) {
        if (code instanceof String) {
            return this.enumValues.get(code);
        }
        if (code.getClass() == this.enumClass) {
            return (Enum) code;
        }
        throw new ClassCastException("Can not cast "
                + "class from " + code.getClass().getName()
                + " to " + this.enumClass.getName());
    }

}
