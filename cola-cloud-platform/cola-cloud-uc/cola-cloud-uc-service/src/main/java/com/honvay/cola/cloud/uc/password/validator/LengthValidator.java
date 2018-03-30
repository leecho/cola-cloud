package com.honvay.cola.cloud.uc.password.validator;

import com.honvay.cola.cloud.uc.password.PasswordValidateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 长度校验器
 * @author LIQIU
 * @date 2018-1-10
 **/
public class LengthValidator implements PasswordValidator {

    /**
     * 最小长度
     */
    private Integer minLength;
    /**
     * 最大长度
     */
    private Integer maxLength;

    @Override
    public PasswordValidateResult validate(String password){
        //判断是否为空
        Assert.isTrue(StringUtils.isNotEmpty(password),"密码不能为空");
        PasswordValidateResult result = new PasswordValidateResult();

        //判断最小长度
        if(minLength != null && password.length() < minLength){
            result.setPassed(false);
            result.setMessage(String.format("密码长度不足，最小长度为:%s，实际长度为：%s",minLength,password.length()));
            return result;
        }

        if(maxLength != null && password.length() > maxLength){
            result.setPassed(false);
            result.setMessage(String.format("密码长度超过限制，最小长度为:%s，实际长度为：%s",maxLength,password.length()));
            return result;
        }

        result.setPassed(true);
        return result;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String toString() {
        return "LengthValidator{" +
                "minLength=" + minLength +
                ", maxLength=" + maxLength +
                '}';
    }
}
