package com.lg.test;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Set;

/**
 * 测试validator的分组校验
 *
 * @author Xulg
 * Created in 2019-09-18 16:54
 */
public class ValidatorGroupTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @SuppressWarnings("all")
    /*
     * group分组功能，只在指定的group下校验该属性
     *      例如
     *          @NotNull(groups = {Update.class}, message = "ID不能为空")
     *          private Integer id;
     *          那么，id属性只有在校验时传入了Update分组才会进行校验，其他分组或不传则不进行校验。
     * 定义分组
     *      最好继承自javax.validation.groups.Default，因为某个属性使用分组功能，其余没使用分组的属性将不能被校验
     *          private interface Update extends Default {
     *          }
     *          private interface Create extends Default {
     *          }
     */

    /*
     * 如果注解中使用了group，那么使用Validator校验的时候必须传入group参数，否则就不校验这个属性了。
     */

    @Test
    public void testWithGroup() {
        AccountWithGroup accountWithGroup = new AccountWithGroup(null, null);
        AccountNoGroup accountNoGroup = new AccountNoGroup(null, null);

        // OK
        Set<ConstraintViolation<AccountWithGroup>> violations1 = validator.validate(accountWithGroup, Create.class);
        for (ConstraintViolation<AccountWithGroup> violation : violations1) {
            System.err.println(violation);
        }

        System.out.println("-----------------------------------------------------------------------------------------");

        // ID不能为空
        Set<ConstraintViolation<AccountWithGroup>> violations2 = validator.validate(accountWithGroup, Update.class);
        for (ConstraintViolation<AccountWithGroup> violation : violations2) {
            System.err.println(violation);
        }

        System.out.println("-----------------------------------------------------------------------------------------");

        // ?
        Set<ConstraintViolation<AccountWithGroup>> violations3 = validator.validate(accountWithGroup);
        for (ConstraintViolation<AccountWithGroup> violation : violations3) {
            System.err.println(violation);
        }

        Set<ConstraintViolation<AccountWithGroup>> violations = validator.validate(accountWithGroup, Default.class);
        for (ConstraintViolation<AccountWithGroup> violation : violations) {
            System.err.println(violation);
        }

        System.out.println("=========================================================================================");

        // ID不能为空
        Set<ConstraintViolation<AccountNoGroup>> violations4 = validator.validate(accountNoGroup);
        for (ConstraintViolation<AccountNoGroup> violation : violations4) {
            System.err.println(violation);
        }

        // 注解中没有使用group，则传入的group参数无效
        Set<ConstraintViolation<AccountNoGroup>> violations5 = validator.validate(accountNoGroup, Update.class);
        for (ConstraintViolation<AccountNoGroup> violation : violations5) {
            System.err.println(violation);
        }
    }

    private static class AccountWithGroup implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotNull(groups = {Update.class}, message = "ID不能为空")
        private Integer id;

        @NotEmpty(message = "用户名不能为空")
        private String username;

        private AccountWithGroup(Integer id, String username) {
            this.id = id;
            this.username = username;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private static class AccountNoGroup implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotNull(message = "ID不能为空")
        private Integer id;

        @NotEmpty(message = "用户名不能为空")
        private String username;

        private AccountNoGroup(Integer id, String username) {
            this.id = id;
            this.username = username;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private interface Update extends Default {
    }

    private interface Create extends Default {

    }

}