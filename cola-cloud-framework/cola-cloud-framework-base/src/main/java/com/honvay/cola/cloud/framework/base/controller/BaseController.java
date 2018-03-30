package com.honvay.cola.cloud.framework.base.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.pagination.PaginationUtils;
import com.honvay.cola.cloud.framework.base.support.MultiDateFormat;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 基础控制类，提供分页数据封装，结果返回功能
 * @author LIQIU
 * @date 2018-1-3
 */
public class BaseController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new MultiDateFormat(), true));
    }

    /**
     *构建成功结果
     * @param msg
     * @param data
     * @return
     */
    public Result success(String msg,Object data) {
        return Result.build(Boolean.TRUE,msg,data );
    }

    /**
     * 构建失败结果
     * @param msg
     * @param data
     * @return
     */
    public Result failure(String msg,Object data){
        return Result.buildFailure(msg ,data);
    }
    /**
     * 构建失败结果
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public Result failure(Integer code,String msg,Object data){
        return Result.buildFailure(code,msg,data);
    }
    /**
     * 构建失败结果
     * @param code
     * @param data
     * @return
     */
    public Result failure(Integer code,Object data){
        return Result.buildFailure(code,null,data);
    }

    /**
     * 构建失败结果
     * @param code
     * @return
     */
    public Result failure(Integer code){
        return Result.buildFailure(code,null,null);
    }
    /**
     * 构建成功结果
     * @return
     */
    public Result success(){
        return success(null,null);
    }

    /**
     * 构建成功结果带信息
     * @param msg
     * @return
     */
    public Result success(String msg){
        return success(msg,null);
    }

    /**
     * 构建成功结果待数据
     * @param data
     * @return
     */
    public Result success(Object data){
        return success(null,data);
    }

    /**
     * 构建失败结果
     * @return
     */
    public Result failure(){
        return failure(0,null,null);
    }

    /**
     * 构建失败结果待数据
     * @param msg
     * @return
     */
    public Result failure(String msg){
        return failure(msg,null);
    }

    /**
     * 构建失败结果带数据
     * @param data
     * @return
     */
    public Result failure(Object data){
        return failure(0,null,data);
    }

    /**
     * 获取分页参数
     * @param <T>
     * @return
     */
    public <T> Page<T> getPagination() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return PaginationUtils.buildFromRequest(request);
    }
}
