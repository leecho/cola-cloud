package com.honvay.cloud.framework.criteria;

import com.honvay.cloud.framework.criteria.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 * @date 2018-4-18
 **/
@Data
@OrderBy(columns = "username")
public class ExampleCriteria implements Criteria<Example> {

    @Like
    private String name;

    @Like
    private String username;

    @Like
    private String phonenNumber;

    @Eq
    private String status;

    @BetweenAnd(columns="createTime",end = "createTimeEnd",defaultEndValue = "2018-10-20")
    private Date createTimeStart;

    private Date createTimeEnd;

    @In(defaultValue = "1,2,3,4,5",columns = "status")
    private String statuses;

    @Like
    private String email;
}
