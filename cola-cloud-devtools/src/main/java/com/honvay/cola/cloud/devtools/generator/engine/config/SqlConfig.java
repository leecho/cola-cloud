package com.honvay.cola.cloud.devtools.generator.engine.config;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 全局配置
 *
 * @author LIQIU
 * @date 2017-05-08 20:21
 */
public class SqlConfig {

    private String sqlPathTemplate;

    private ContextConfig contextConfig;

    private Connection connection;

    private String parentMenuName;


    public void init() {

        this.sqlPathTemplate = "\\src\\main\\java\\{}.sql";

        if(parentMenuName == null){
            return ;
        }

        //根据父菜单查询数据库中的pcode和pcodes
        String[] pcodeAndPcodes = getPcodeAndPcodes();
        if (pcodeAndPcodes == null) {
            System.err.println("父级菜单名称输入有误!!!!");
            return;
        }
    }

    public String[] getPcodeAndPcodes() {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select * from cola_sys_permission where name like ?");
            preparedStatement.setString(1, "%" + parentMenuName + "%");
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                String pcode = String.valueOf(results.getLong("perm_id"));
                String pcodes = results.getString("pids");
                if (StringUtils.isNotEmpty(pcode) && StringUtils.isNotEmpty(pcodes)) {
                    String[] strings = {pcode, pcodes};
                    return strings;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getSqlPathTemplate() {
        return sqlPathTemplate;
    }

    public void setSqlPathTemplate(String sqlPathTemplate) {
        this.sqlPathTemplate = sqlPathTemplate;
    }

}
