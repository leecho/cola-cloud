package com.honvay.cola.cloud.dict.client.jackson;

import com.honvay.cola.cloud.dict.model.DictVO;

import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
public class JacksonDictContext {

    public static ThreadLocal<Map<String,Map<String,DictVO>>> repository = new ThreadLocal<>();

    public static void put(Map<String,Map<String,DictVO>> content){
        repository.set(content);
    }

    public static String getName(String code, String value){
        if(repository.get() == null){
            return null;
        }
        Map<String,DictVO> dict = repository.get().get(code);
        if(dict != null){
            DictVO dictVO = dict.get(value);
            if(dictVO != null){
                return dictVO.getName();
            }
        }
        return null;
    }

    public static void clear(){
        repository.remove();
    }
}
