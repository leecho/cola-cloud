package com.honvay.cola.cloud.dict.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LIQIU
 * @date 2018-1-22
 **/
@Data
public class DictItemVO implements Serializable {
    
    private String  code;
    private String  name;
    private String  value;
    private Integer orderNo;

}
