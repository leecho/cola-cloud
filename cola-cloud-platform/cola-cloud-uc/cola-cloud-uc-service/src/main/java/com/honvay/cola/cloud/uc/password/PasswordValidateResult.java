package com.honvay.cola.cloud.uc.password;

/**
 * 密码验证结果
 * @author LIQIU
 * @date 2018-1-10
 **/
public class PasswordValidateResult {
    private boolean passed;
    private String message;

    public boolean getPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
