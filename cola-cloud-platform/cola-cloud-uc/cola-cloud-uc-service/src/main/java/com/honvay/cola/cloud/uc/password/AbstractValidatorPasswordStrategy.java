package com.honvay.cola.cloud.uc.password;


import com.honvay.cola.cloud.uc.password.validator.PasswordValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LIQIU
 * @date 2018-1-10
 **/
public abstract class AbstractValidatorPasswordStrategy implements PasswordStrategy{


    protected List<PasswordValidator> validators = new ArrayList<PasswordValidator>();

    @Override
    public PasswordValidateResult validate(String password) {
        PasswordValidateResult result = new PasswordValidateResult();
        for (PasswordValidator validator : validators){
            PasswordValidateResult validateResult = validator.validate(password);
            if(!validateResult.getPassed()){
                return validateResult;
            }
        }
        result.setPassed(true);
        return result;

    }

    public List<PasswordValidator> getValidators() {
        return validators;
    }

    public void setValidators(List<PasswordValidator> validators) {
        this.validators = validators;
    }

    public void addValidator(PasswordValidator passwordValidator){
        this.validators.add(passwordValidator);
    }

    public void removeValidator(PasswordValidator passwordValidator){
        this.validators.remove(passwordValidator);
    }
}
