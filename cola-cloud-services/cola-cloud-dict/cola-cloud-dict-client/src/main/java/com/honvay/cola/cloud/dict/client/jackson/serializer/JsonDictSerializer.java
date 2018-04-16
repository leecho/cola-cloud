package com.honvay.cola.cloud.dict.client.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.honvay.cola.cloud.dict.client.DictClient;
import com.honvay.cola.cloud.dict.client.jackson.JacksonDictContext;
import com.honvay.cola.cloud.dict.client.jackson.annotation.JsonDict;
import com.honvay.cola.cloud.dict.client.jackson.annotation.JsonDictField;
import com.honvay.cola.cloud.dict.model.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Slf4j
public class JsonDictSerializer extends BeanSerializer {

    private JsonDict jsonDict;

    private DictClient dictClient;

    public JsonDictSerializer(BeanSerializerBase src, DictClient dictClient, JsonDict jsonDict) {
        super(src);
        this.jsonDict = jsonDict;
        this.dictClient = dictClient;
    }

    @Override
    protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (dictClient == null) {
            throw new IllegalArgumentException("Dict Client must not be null");
        }
        try {
            //根据预设的数据字典编号从缓存中获取数据
            Map<String, Map<String, DictVO>> dicts = new HashMap<>();
            for (String dictCode : jsonDict.dictCodes()) {
                dicts.put(dictCode, dictClient.getByParent(dictCode));
            }
            JacksonDictContext.put(dicts);

            super.serializeFields(bean, gen, provider);

            //处理自定义的字段
            try {
                JsonDictField[] fields = jsonDict.dictFields();
                for (JsonDictField dictField : fields) {
                    Object value = PropertyUtils.getProperty(bean, dictField.valueField());
                    gen.writeFieldName(dictField.name());
                    gen.writeString(JacksonDictContext.getName(dictField.dictCode(), (String) value));
                }
            } catch (Exception e) {
                log.error("序列化数据字典发生错误：" + e);
            }
        } finally {
            JacksonDictContext.clear();
        }
    }
}
