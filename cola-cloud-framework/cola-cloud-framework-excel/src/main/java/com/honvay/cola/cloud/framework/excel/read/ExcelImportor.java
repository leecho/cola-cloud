package com.honvay.cola.cloud.framework.excel.read;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.honvay.cola.cloud.framework.excel.read.handler.SimpleImportHandler;
import com.honvay.cola.cloud.framework.excel.exception.ExcelProcessException;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * excel导入工具
 *
 * @author fengshuonan
 * @date 2017-11-29-下午5:42
 */
public class ExcelImportor {

    /**
     * 表头的key-name对应的map
     * key为程序中的字段名,name为实际导出excel中的表头(name可以为中文字段名)
     */
    private Map<String, String> headerKeyNameMap;

    /**
     * 表格标题行数,默认0
     */
    private int titleRows = 0;
    /**
     * 表头行数,默认1
     */
    private int headRows = 1;

    /**
     * 导入的文件
     */
    private File importFile;

    public ExcelImportor(Map<String, String> headerKeyNameMap) {
        this.headerKeyNameMap = headerKeyNameMap;
        if (this.headerKeyNameMap == null || this.importFile == null) {
            throw new ExcelProcessException("导入excel传入的表头信息或excel文件为空");
        }
    }

    private ImportParams buildImportParams(){
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headRows);
        params.setDataHanlder(new SimpleImportHandler(headerKeyNameMap));
        return params;
    }

    public List<Map<String,Object>> importExcel(File file){
        ImportParams params = this.buildImportParams();
        return ExcelImportUtil.importExcel(file, Map.class, params);
    }

    public List<Map<String,Object>> importExcel(InputStream input){
        ImportParams params = this.buildImportParams();
        try {
            return ExcelImportUtil.importExcel(input, Map.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<String, String> getHeaderKeyNameMap() {
        return headerKeyNameMap;
    }

    public void setHeaderKeyNameMap(Map<String, String> headerKeyNameMap) {
        this.headerKeyNameMap = headerKeyNameMap;
    }

    public int getTitleRows() {
        return titleRows;
    }

    public void setTitleRows(int titleRows) {
        this.titleRows = titleRows;
    }

    public int getHeadRows() {
        return headRows;
    }

    public void setHeadRows(int headRows) {
        this.headRows = headRows;
    }

}
