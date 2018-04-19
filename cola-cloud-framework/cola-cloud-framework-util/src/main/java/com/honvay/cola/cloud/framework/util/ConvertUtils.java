package com.honvay.cola.cloud.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LIQIU
 * @date 2018-4-19
 **/
public class ConvertUtils extends org.apache.commons.beanutils.ConvertUtils {

    public static Object convert(final String value, final Class<?> clazz) {
        Object result = null;
        if(clazz.equals(Date.class)){
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                result = dateTimeFormat.parse(value);
            } catch (ParseException e) {
            }

            if(result == null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    result = dateFormat.parse(value);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("The value " + value + " can't convert to Date");
                }
            }

            return result;

        }else{
            return org.apache.commons.beanutils.ConvertUtils.convert(value,clazz);
        }
    }
}
