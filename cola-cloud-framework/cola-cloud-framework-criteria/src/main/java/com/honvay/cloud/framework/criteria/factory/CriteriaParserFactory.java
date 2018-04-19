package com.honvay.cloud.framework.criteria.factory;

import com.baomidou.mybatisplus.toolkit.ReflectionKit;
import com.honvay.cloud.framework.criteria.parser.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LIQIU
 * @date 2018-4-19
 **/
@Slf4j
public class CriteriaParserFactory {

    private static Map<Class<?>, ConditionParser<?>> repository = new ConcurrentHashMap<>();

    static {
        register(new BetweenAndParser());
        register(new LikeParser());
        register(new LtParser());
        register(new LeParser());
        register(new GeParser());
        register(new GtParser());
        register(new EqParser());
        register(new InParser());
    }

    /**
     * 获取解析器
     * @param clazz
     * @return
     */
    public static ConditionParser<?> getParser(Class<?> clazz) {
        return repository.get(clazz);
    }

    /**
     * 注册解析器
     * @param conditionParser
     */
    public static void register(ConditionParser conditionParser) {
        Class<?> type = ReflectionKit.getSuperClassGenricType(conditionParser.getClass(), 0);
        if (log.isDebugEnabled()) {
            log.debug("CriteriaContext Parser {0} was registered on {1}", conditionParser, type);
        }
        repository.put(type, conditionParser);
    }

}
