package com.honvay.cola.cloud.framework.excel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.honvay.cola.cloud.framework.excel.exception.ExcelProcessException;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <pre>
 * 报表导出的抽象类
 *
 * 导入导出封装开源框架easypoi(https://gitee.com/lemur/easypoi)
 * 文档: http://www.afterturn.cn/doc/easypoi.html
 * </pre>
 *
 * @author fengshuonan
 * @date 2017年11月29日17:36:05
 */
public class ExcelExportor {

    /**
     * excel标题
     */
    protected String title;

    /**
     * 表头的key-name对应的map
     * key为程序中的字段名,name为实际导出excel中的表头(name可以为中文字段名)
     */
    protected Map<String, String> headKeyNameMap;

    /**
     * 导出的数据
     */
    protected List<Object> list;

    /**
     * 导出的数据(转化成map后)
     */
    protected List<Map<String, Object>> preExportData;

    /**
     * easypoi所需表头的实体
     */
    protected ArrayList<ExcelExportEntity> excelExportEntities = new ArrayList<>();

    public ExcelExportor(String title, Map<String, String> headKeyNameMap, List<Object> list) {
        this.title = title;
        this.headKeyNameMap = headKeyNameMap;
        this.list = list;
    }

    public void export(File file){
        try {
            FileOutputStream output = new FileOutputStream(file);
            this.export(output);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出的过程
     */
    public void export(OutputStream output) {
        if (headKeyNameMap == null || list == null) {
            throw new ExcelProcessException("字段集或数据集为空!");
        }
        Set<Map.Entry<String, String>> entries = headKeyNameMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            excelExportEntities.add(new ExcelExportEntity(entry.getValue(), entry.getKey()));
        }
        preExport();
        doExport(output);
    }

    /**
     * 报表导出前的工作: 例如格式化数据等
     */
    @SuppressWarnings("unchecked")
	public void preExport() {
        preExportData = new ArrayList<>();
        for (Object bean : list) {
            if (bean instanceof Map) {
                preExportData.add((Map<String, Object>) bean);
            } else {
                Map<String, Object> stringObjectMap = new HashMap<String,Object>();
                try {
					BeanUtils.populate(bean, stringObjectMap);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
                preExportData.add(stringObjectMap);
            }
        }
    }

    /**
     * 自定义导出的方式
     */
    protected void doExport(OutputStream output){
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, title), this.excelExportEntities, preExportData);
            workbook.write(output);
        } catch (IOException e) {
            throw new ExcelProcessException("导出excel到文件流异常!", e);
        }
    };
}
