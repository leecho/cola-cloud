package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cloud.framework.criteria.CriteriaContext;
import com.honvay.cloud.framework.criteria.annotation.Order;
import com.honvay.cloud.framework.criteria.annotation.OrderBy;
import com.honvay.cloud.framework.criteria.factory.CriteriaParserFactory;
import com.honvay.cloud.framework.criteria.observer.Observer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * 条件解析器
 *
 * @author LIQIU
 * @date 2018-4-17
 **/
@Slf4j
public class CriteriaParser {

    public <T> EntityWrapper<T> parse(Criteria<T> criteria) {
        return this.parse(criteria, new EntityWrapper<T>(), null);
    }

    public <T> EntityWrapper parse(Criteria<T> criteria, EntityWrapper<T> wrapper) {
        return this.parse(criteria, wrapper, null);
    }

    /**
     * 分组解析条件
     *
     * @param criteria
     * @param wrapper
     * @param group
     * @param <T>
     * @return
     */
    public <T> EntityWrapper parse(Criteria<T> criteria, EntityWrapper<T> wrapper, String group) {
        Field[] fields = criteria.getClass().getDeclaredFields();

        //先对顺序进行排序
        this.sort(fields);

        Observer<T> observer = null;
        if (criteria instanceof Observer) {
            observer = (Observer) criteria;
        }

        if (observer != null) {
            observer.beforeParse(wrapper);
        }

        //属性
        String property;
        //值
        Object value;


        //遍历字段
        for (Field field : fields) {
            //遍历字段的注解
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations.length == 0) {
                continue;
            }

            property = field.getName();
            try {
                field.setAccessible(true);
                value = field.get(criteria);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Get value of property :" + property + " on " + criteria + " error", e);
            }

            for (Annotation annotation : annotations) {
                //组装Condition数据
                ConditionParser parser = CriteriaParserFactory.getParser(annotation.annotationType());
                //有解释器且通过拦截器
                if (parser == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("No parser for annotation :" + annotation);
                    }
                    continue;
                }

                //判断是否启用
                if (!parser.isEnable(annotation)) {
                    if (log.isDebugEnabled()) {
                        log.debug("The conidtion {} of property {} in {} is disabled :" + annotation, property, criteria);
                    }
                    continue;
                }

                //判断是否为空
                if (isEmpty(value) && !parser.allowEmpty(annotation)) {
                    if (log.isDebugEnabled()) {
                        log.debug("The value of property {} in {} is null and not allow empty :" + property, criteria);
                    }
                    continue;
                }

                //判断分组
                if (StringUtils.equals(parser.getGroup(annotation), group)) {
                    if (log.isDebugEnabled()) {
                        log.debug("The group of criteria {} for {} in {} is {} and special group is  :" + annotation, property, criteria, parser.getGroup(annotation), group);
                    }
                    continue;
                }

                CriteriaContext criteriaContext = new CriteriaContext(annotation, value, property, criteria, field.getType());
                boolean toParse = true;
                if (observer != null) {
                    //观察者过滤
                    toParse = observer.onParse(criteriaContext, wrapper);
                }
                if (toParse) {
                    //解析条件
                    parser.parse(criteriaContext, wrapper);
                }
            }
        }

        this.parseOrderBy(criteria, wrapper, group);

        if (observer != null) {
            observer.afterParse(wrapper);
        }
        return wrapper;
    }

    /**
     * 解析排序
     *
     * @param criteria
     * @param entityWrapper
     * @param group
     */
    private void parseOrderBy(Criteria<?> criteria, EntityWrapper entityWrapper, String group) {
        OrderBy[] orderbies = criteria.getClass().getAnnotationsByType(OrderBy.class);
        Arrays.stream(orderbies).filter(orderBy -> StringUtils.equals(StringUtils.trimToNull(orderBy.group()), group) && orderBy.enable()).forEach(orderBy -> {
            entityWrapper.orderBy(StringUtils.join(orderBy.columns(), ","), orderBy.isAsc());
        });
    }

    /**
     * 将字段进行排序
     *
     * @param fields
     */
    private void sort(Field[] fields) {
        Arrays.sort(fields, (f1, f2) -> {
            int order1 = 0, order2 = 0;
            Order criteriaOrder1 = f1.getAnnotation(Order.class);
            Order criteriaOrder2 = f2.getAnnotation(Order.class);
            if (criteriaOrder1 != null) {
                order1 = criteriaOrder1.value();
            }
            if (criteriaOrder2 != null) {
                order2 = criteriaOrder2.value();
            }
            return order1 - order2;
        });
    }

    /**
     * 判断值是否为空
     *
     * @param value
     * @return
     */
    private boolean isEmpty(Object value) {
        if (value instanceof String) {
            return StringUtils.isEmpty((String) value);
        }
        return Objects.isNull(value);
    }
}
