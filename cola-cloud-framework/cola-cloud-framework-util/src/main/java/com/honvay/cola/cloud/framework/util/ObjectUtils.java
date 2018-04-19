package com.honvay.cola.cloud.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author LIQIU
 * @date 2018-4-19
 **/
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils{

    public static boolean isNull(Object value){
        if(value instanceof String){
            return StringUtils.isEmpty((String)value);
        }
        return Objects.isNull(value);
    }
}
