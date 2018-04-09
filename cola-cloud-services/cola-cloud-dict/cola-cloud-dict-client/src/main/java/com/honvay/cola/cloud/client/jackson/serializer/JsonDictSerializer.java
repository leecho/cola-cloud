package com.honvay.cola.cloud.client.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.honvay.cola.cloud.client.DictClient;
import com.honvay.cola.cloud.client.jackson.JacksonDictContext;
import com.honvay.cola.cloud.dict.model.DictVO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Slf4j
public class JsonDictSerializer extends BeanSerializer {

    private String[] dictCodes;

    private DictClient dictClient;

    public JsonDictSerializer(BeanSerializerBase src, DictClient dictClient, String[] dictCodes) {
        super(src);
        this.dictClient = dictClient;
        this.dictCodes = dictCodes;
    }

    @Override
    protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        System.out.println("加载数据字典:" + dictCodes);
        if (dictClient == null) {
            throw new IllegalArgumentException("Dict Client must not be null");
        }

        //根据预设的数据字典编号从缓存中获取数据
        Map<String, Map<String, DictVO>> dicts = new HashMap<>();
        for (String dictCode : dictCodes) {
            dicts.put(dictCode, dictClient.getByParent(dictCode));
        }
        JacksonDictContext.put(dicts);
        try {
            super.serializeFields(bean, gen, provider);
        } finally {
            JacksonDictContext.clear();
        }
    }
}
