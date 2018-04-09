package com.honvay.cola.cloud.dict.client.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.honvay.cola.cloud.dict.client.jackson.JacksonDictContext;
import com.honvay.cola.cloud.dict.client.jackson.annotation.JsonDictName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Slf4j
public final class JsonDictNameSerializer extends NonTypedScalarSerializerBase<Object> {

    public JsonDictNameSerializer() {
        super(String.class, false);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Object bean = gen.getOutputContext().getCurrentValue();
        String property = gen.getOutputContext().getCurrentName();
        try {
            Field field = bean.getClass().getDeclaredField(property);
            JsonDictName dictName = field.getAnnotation(JsonDictName.class);
            if (StringUtils.hasText(dictName.valueField())) {
                value = PropertyUtils.getProperty(bean, dictName.valueField());
            }
            gen.writeString(JacksonDictContext.getName(dictName.dictCode(), (String) value));
        } catch (Exception e) {
            log.error("序列化数据字典发生错误：" + e);
        }
    }
}
