package com.lg.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * 测试validator的嵌套校验
 *
 * @author Xulg
 * Created in 2019-09-19 15:23
 */
public class NestingValidatorTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void test() {
        Set<ConstraintViolation<Account>> violations1 = validator.validate(new Account(
                null, null, null));
        for (ConstraintViolation<Account> violation : violations1) {
            System.err.println(violation);
        }
        System.out.println("=====================================================");
        Set<ConstraintViolation<Account>> violations2 = validator.validate(new Account(
                null, null, new Address(null)));
        for (ConstraintViolation<Account> violation : violations2) {
            System.err.println(violation);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Account implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotNull(message = "id不能为空")
        private Integer id;

        @NotEmpty(message = "username不能为空")
        private String username;

        @NotNull(message = "address不能为空")
        @Valid
        private Address address;
    }

    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Address implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotEmpty(message = "详情地址不能为空")
        private String addressInfo;
    }
}