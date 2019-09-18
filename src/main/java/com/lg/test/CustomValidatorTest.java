package com.lg.test;

import com.lg.annotation.CheckEnum;
import com.lg.annotation.Mobile;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Set;

/**
 * 测试自定义的validator校验器
 *
 * @author Xulg
 * Created in 2019-09-18 14:36
 */
public class CustomValidatorTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testCheckEnumValidator() {
        class Account implements Serializable {
            private static final long serialVersionUID = 1L;

            private String name;

            @CheckEnum(value = Sex.class, whitelist = {"man", "female"}, message = "性别只能是man,female")
            private String sex;

            private Account(String name, String sex) {
                this.name = name;
                this.sex = sex;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }
        }

        Account account1 = new Account("张三", "man");
        Account account2 = new Account("李四", "female");
        Account account3 = new Account("王五", "Man");
        Account account4 = new Account("赵六", "freak");

        // OK
        Set<ConstraintViolation<Account>> violations1 = validator.validate(account1);
        for (ConstraintViolation<Account> violation : violations1) {
            System.err.println(violation);
        }
        // OK
        Set<ConstraintViolation<Account>> violations2 = validator.validate(account2);
        for (ConstraintViolation<Account> violation : violations2) {
            System.err.println(violation);
        }

        // 不OK
        Set<ConstraintViolation<Account>> violations3 = validator.validate(account3);
        for (ConstraintViolation<Account> violation : violations3) {
            System.err.println(violation);
        }

        // 不OK
        Set<ConstraintViolation<Account>> violations4 = validator.validate(account4);
        for (ConstraintViolation<Account> violation : violations4) {
            System.err.println(violation);
        }
    }

    @Test
    public void testMobileValidator() {
        // 手机号校验宽松模式
        class Account1 implements Serializable {
            private static final long serialVersionUID = 1L;

            @Mobile(message = "手机号格式不正确", isStrict = false)
            private String mobile;

            private Account1(String mobile) {
                this.mobile = mobile;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }

        // 手机号校验严格模式
        class Account2 implements Serializable {
            private static final long serialVersionUID = 1L;

            @Mobile(message = "手机号格式不正确", isStrict = true)
            private String mobile;

            private Account2(String mobile) {
                this.mobile = mobile;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }

        String correctMobile = "15268848621";
        String incorrectMobile1 = "10000000000";
        String incorrectMobile2 = "1000000000";

        // 正确 宽松
        Set<ConstraintViolation<Account1>> violations1 = validator.validate(new Account1(correctMobile));
        for (ConstraintViolation<Account1> violation : violations1) {
            System.err.println(violation);
        }

        // 正确 严厉
        Set<ConstraintViolation<Account2>> violations2 = validator.validate(new Account2(correctMobile));
        for (ConstraintViolation<Account2> violation : violations2) {
            System.err.println(violation);
        }

        // 正确 宽松
        Set<ConstraintViolation<Account1>> violations3 = validator.validate(new Account1(incorrectMobile1));
        for (ConstraintViolation<Account1> violation : violations3) {
            System.err.println(violation);
        }

        // 错误 严厉
        Set<ConstraintViolation<Account2>> violations4 = validator.validate(new Account2(incorrectMobile1));
        for (ConstraintViolation<Account2> violation : violations4) {
            System.err.println(violation);
        }

        // 错误 宽松
        Set<ConstraintViolation<Account1>> violations5 = validator.validate(new Account1(incorrectMobile2));
        for (ConstraintViolation<Account1> violation : violations5) {
            System.err.println(violation);
        }

        // 错误 严厉
        Set<ConstraintViolation<Account2>> violations6 = validator.validate(new Account2(incorrectMobile2));
        for (ConstraintViolation<Account2> violation : violations6) {
            System.err.println(violation);
        }
    }

    public static enum Sex {

        /**
         * 男
         */
        MAN("man", "男"),

        /**
         * 女
         */
        FEMALE("female", "男"),

        /**
         * 鹅
         */
        FREAK("freak", "鹅"),;

        private String code;

        private String description;

        Sex(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static Sex getByCode(String code) {
            if (code == null) {
                return null;
            }
            for (Sex value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }
}