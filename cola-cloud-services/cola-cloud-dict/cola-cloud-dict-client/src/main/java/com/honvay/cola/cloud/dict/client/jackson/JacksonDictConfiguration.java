package com.honvay.cola.cloud.dict.client.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.honvay.cola.cloud.dict.client.DictClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 数据字典转换配置
 * @author LIQIU
 * @date 2018-4-9
 **/
@Configuration
public class JacksonDictConfiguration extends WebMvcConfigurerAdapter {

    @Autowired(required = false)
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private DictClient dictClient;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        if(mappingJackson2HttpMessageConverter != null){
            ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
            SimpleModule module = new SimpleModule("JacksonDictModule");
            JacksonDictJsonModifier jacksonDictJsonModifier = new JacksonDictJsonModifier();
            jacksonDictJsonModifier.setDictClient(dictClient);
            module.setSerializerModifier(jacksonDictJsonModifier);
            objectMapper.registerModule(module);
        }
    }
}
