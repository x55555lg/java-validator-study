package com.lg.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Iterables;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基于validator框架的校验工具
 *
 * @author Xulg
 * Created in 2019-09-18 11:05
 */
@SuppressWarnings("ConstantConditions")
public class ValidatorUtils {
    private static Validator validator;

    static {
        try {
            if (false) {
                // 开启快速结束模式failFast(true)
                validator = Validation.byProvider(HibernateValidator.class).configure()
                        .failFast(false).buildValidatorFactory().getValidator();
            } else {
                validator = Validation.buildDefaultValidatorFactory().getValidator();
            }
        } catch (ValidationException e) {
            throw new Error(e);
        }
    }

    private ValidatorUtils() {
    }

    /**
     * 断言验证
     *
     * @param bean   the target bean
     * @param groups the validate groups
     * @param <T>    the target bean type
     * @throws BusinessException e
     */
    public static <T> void assertValidate(T bean, Class<?>... groups) throws BusinessException {
        Set<ConstraintViolation<T>> violations = validator.validate(bean, groups);
        ConstraintViolation<T> violation = Iterables.getFirst(violations, null);
        if (violation != null) {
            String property = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                    violation.getPropertyPath().toString());
            throw new BusinessException("err_" + property, violation.getMessage());
        }
    }

    /**
     * 校验对象
     *
     * @param bean   the target bean
     * @param groups the validate groups
     * @return the valid result
     */
    public static <T> ValidResult validateBean(T bean, Class<?>... groups) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(bean, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验bean的某一个属性
     *
     * @param bean         bean
     * @param propertyName 属性名称
     * @param groups       the validate groups
     * @return the valid result
     */
    public static <T> ValidResult validateProperty(T bean, String propertyName, Class<?>... groups) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validateProperty(bean, propertyName, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(propertyName, violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验结果类
     */
    @SuppressWarnings("all")
    public static class ValidResult {

        /**
         * 是否有错误
         */
        private boolean hasErrors;

        /**
         * 错误信息
         */
        private List<ErrorMessage> errors;

        private ValidResult() {
            this.errors = new ArrayList<>();
        }

        public boolean isHasErrors() {
            return hasErrors;
        }

        public void setErrors(List<ErrorMessage> errors) {
            this.errors = errors;
        }

        public boolean hasErrors() {
            return hasErrors;
        }

        public void setHasErrors(boolean hasErrors) {
            this.hasErrors = hasErrors;
        }

        /**
         * 获取所有验证信息
         *
         * @return 集合形式
         */
        public List<ErrorMessage> getAllErrors() {
            return errors;
        }

        /**
         * 获取所有验证信息
         *
         * @return 字符串形式
         */
        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            for (ErrorMessage error : errors) {
                sb.append(error.getPropertyPath()).append(":").append(error.getMessage()).append(" ");
            }
            return sb.toString();
        }

        public void addError(String propertyName, String message) {
            this.errors.add(new ErrorMessage(propertyName, message));
        }
    }

    @SuppressWarnings("all")
    public static class ErrorMessage {

        private String propertyPath;

        private String message;

        public ErrorMessage() {
        }

        private ErrorMessage(String propertyPath, String message) {
            this.propertyPath = propertyPath;
            this.message = message;
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        public void setPropertyPath(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}