package com.honvay.cola.cloud.framework.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtils {

    /**
     * 使用hibernate的注解来进行验证
     *
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * 校验对象，当没有通过校验而且throwException=true时则会抛出异常
     *
     * @param obj 验证对象
     * @param throwException 是否抛出异常
     */
    public static <T> Set<ConstraintViolation<T>> validate(T obj,boolean throwException) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if(throwException && constraintViolations.size() > 0){
            throw  new ConstraintViolationException(constraintViolations);
        }
        return constraintViolations;
    }

    /**
     * 校验对象
     * @param obj
     */
    public static <T> Set<ConstraintViolation<T>> validate(T obj) {
        return validate(obj,false);
    }
}
