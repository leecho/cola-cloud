package com.honvay.cola.cloud.uc.password.validator;

import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.uc.password.PasswordValidateResult;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码强度校验器
 * @author LIQIU
 * @date 2018-1-10
 **/
public class StrengthValidator implements PasswordValidator{

    /**
     * 简单的密码，任意字母和数字
     */
    public static final Integer PASSWORD_STRENGTH_SIMPLE = 1;
    /**
     * 普通的密码，字母加数字
     */
    public static final Integer PASSWORD_STRENGTH_NORMAL = 2;
    /**
     * 复杂的密码，大小写英文加数字
     */
    public static final Integer PASSWORD_STRENGTH_COMPLEX = 3;
    /**
     * 安全的密码，大小写英文加数字加特殊字符
     */
    public static final Integer PASSWORD_STRENGTH_SAFE = 4;

    private Map<Integer,String> messages = new HashMap<>();

    {
        messages.put(PASSWORD_STRENGTH_SIMPLE,"任意字符");
        messages.put(PASSWORD_STRENGTH_NORMAL,"英文字母加数字");
        messages.put(PASSWORD_STRENGTH_COMPLEX,"大小写英文字母加数字");
        messages.put(PASSWORD_STRENGTH_SAFE,"大小写英文字母加数字加特殊字符");
    }

    private Integer strength;

    @Override
    public PasswordValidateResult validate(String password) {
        Assert.isTrue(strength != null,"密码强度属性没有配置");
        Assert.isTrue(StringUtils.isNotEmpty(password),"密码不能为空");
        int checkedStrenght = this.getPasswordStrength(password);
        PasswordValidateResult result = new PasswordValidateResult();
        if(checkedStrenght < strength){
            result.setPassed(false);
            result.setMessage(String.format("密码强度不足，密码要求为：%s", messages.get(strength)));
            return result;
        }
        result.setPassed(true);
        return result;
    }

    /**
     * 获取密码强度
     * @param password
     * @return
     */
    private int getPasswordStrength(String password){
        int strength = 0;
        if(password.matches(".*\\d+.*")){ strength ++ ;}
        if(password.matches(".*[a-z]+.*")) {strength ++ ;}
        if(password.matches(".*[A-Z]+.*")) {strength ++ ;}
        if(password.matches(".*\\W+.*")) {strength ++ ;}
        return strength;

    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return "StrengthValidator{" +
                "strength=" + strength +
                '}';
    }
}
