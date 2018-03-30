package com.honvay.cola.cloud.uc.password;


import com.honvay.cola.cloud.uc.password.validator.LengthValidator;
import com.honvay.cola.cloud.uc.password.validator.StrengthValidator;

/**
 * 默认密码策略
 * @author LIQIU
 * @date 2018-1-10
 **/
public abstract class LengthAndStrenghtPasswordStrategy extends AbstractValidatorPasswordStrategy{

    private Integer minLength;
    private Integer maxLength;
    private Integer strength;

    private LengthValidator lengthValidator = new LengthValidator();
    private StrengthValidator strengthValidator = new StrengthValidator();

    public LengthAndStrenghtPasswordStrategy(){
        this.validators.add(this.lengthValidator);
        this.validators.add(this.strengthValidator);
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.lengthValidator.setMinLength(minLength);
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.lengthValidator.setMaxLength(maxLength);
        this.maxLength = maxLength;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strengthValidator.setStrength(strength);
        this.strength = strength;
    }
}
