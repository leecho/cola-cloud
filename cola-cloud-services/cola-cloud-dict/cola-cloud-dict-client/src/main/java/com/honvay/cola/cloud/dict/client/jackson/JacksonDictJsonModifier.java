package com.honvay.cola.cloud.dict.client.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.honvay.cola.cloud.dict.client.DictClient;
import com.honvay.cola.cloud.dict.client.jackson.annotation.JsonDict;
import com.honvay.cola.cloud.dict.client.jackson.serializer.JsonDictSerializer;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
public class JacksonDictJsonModifier extends BeanSerializerModifier {

    private DictClient dictClient;

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                              BeanDescription beanDesc, JsonSerializer<?> serializer) {
        JsonDict jsonDict = beanDesc.getBeanClass().getAnnotation(JsonDict.class);
        if(jsonDict != null){
            serializer = new JsonDictSerializer((BeanSerializerBase) serializer, dictClient,jsonDict);
        }
        return serializer;
    }

    public DictClient getDictClient() {
        return dictClient;
    }

    public void setDictClient(DictClient dictClient) {
        this.dictClient = dictClient;
    }
}
