package com.honvay.cloud.framework.criteria;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.parser.CriteriaParser;
import org.junit.Test;

import java.util.Date;

/**
 * @author LIQIU
 * @date 2018-4-18
 **/
public class QueryParseTest {

    @Test
    public void testParser(){
        CriteriaParser criteriaParser = new CriteriaParser();
        ExampleCriteria criteria = new ExampleCriteria();
        criteria.setName("123132");
        criteria.setStatus("1");
        criteria.setCreateTimeStart(new Date());
        EntityWrapper<Example> entityWrapper = criteriaParser.parse(criteria);
        System.out.println(entityWrapper);
    }

}
