package com.honvay.cola.cloud.devtools.generator.service;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.honvay.cola.cloud.framework.util.StringUtils;
import lombok.Data;

import java.util.List;

/**
 * @author LIQIU
 * @date 2018-4-3
 **/
@Data
public class EntityNameRewriter extends InjectionConfig {

    private String entityName;

    @Override
    public void initMap() {
        if(StringUtils.isNotEmpty(entityName)){
            List<TableInfo> tableInfoList = this.getConfig().getTableInfoList();
            if(tableInfoList != null){
                tableInfoList.get(0).setComment(entityName);
            }
        }
    }
}
