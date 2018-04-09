package com.honvay.cola.cloud.dict.client.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.honvay.cola.cloud.dict.client.jackson.serializer.JsonDictNameSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注册作用在于告诉转换器通过valueField的配置获取字段值，再在dictCode中找到对应的名称
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
 *  {@link com.honvay.cola.cloud.dict.client.jackson.serializer.JsonDictNameSerializer}，在这个类中会处理这个注解
 *
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonDictNameSerializer.class,nullsUsing = JsonDictNameSerializer.class)
public @interface JsonDictName {
    /**
     * 数据字段的值字段，应为往往值字段和文本字段是两个字段
     * @return
     */
    String valueField() default "";

    /**
     * 数据字典代码
     * @return
     */
    String dictCode();
}
