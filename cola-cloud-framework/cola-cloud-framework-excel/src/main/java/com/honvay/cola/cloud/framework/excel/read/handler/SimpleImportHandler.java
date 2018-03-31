package com.honvay.cola.cloud.framework.excel.read.handler;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import com.honvay.cola.cloud.framework.excel.exception.ExcelProcessException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Excel导入的处理器
 *
 * @author fengshuonan
 * @Date 2017/11/29 下午5:43
 */
public class SimpleImportHandler extends ExcelDataHandlerDefaultImpl<Map<String, Object>> {

    /**
     * 表头的key-name对应的map
     * key为程序中的字段名,name为实际导出excel中的表头(name可以为中文字段名)
     */
    private Map<String, String> headerKeyNameMap;

    public SimpleImportHandler(Map<String, String> headerKeyNameMap) {
        this.headerKeyNameMap = headerKeyNameMap;
        if(this.headerKeyNameMap == null){
            throw new ExcelProcessException("导入excel传入的表头信息为空");
        }
    }

    @Override
    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        if (value instanceof Date) {
            map.put(getRealKey(originKey), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((Date) value)));
        } else {
            map.put(getRealKey(originKey), value != null ? value.toString() : null);
        }
    }

    private String getRealKey(String originKey) {
        Set<Map.Entry<String, String>> entries = headerKeyNameMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (originKey.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return originKey;
    }
}
