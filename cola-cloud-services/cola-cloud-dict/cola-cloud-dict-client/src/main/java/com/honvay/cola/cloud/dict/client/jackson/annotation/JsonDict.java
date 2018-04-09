package com.honvay.cola.cloud.dict.client.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JsonDict注解告诉Jackson转化器在转换之前先从缓存总预先加载数据字典数据处理，用于字段转换
 * 示例
 * <pre>
 *      @JsonDict("YN");
 *      public class OrderVO {
 *
 *          private String enable;
 *
 *          @JsonDictName(dictCode = "YN" ,valueField = "enable");
 *          private String enableName;
 *
 *      }
 * </pre>
 *
 *  {@link com.honvay.cola.cloud.dict.client.jackson.serializer.JsonDictSerializer}，在这个类中会处理这个注解
 *
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE,
        ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface JsonDict {
    String[] value();
}
