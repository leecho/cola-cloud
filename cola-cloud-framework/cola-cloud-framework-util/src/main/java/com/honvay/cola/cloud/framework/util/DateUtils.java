package com.honvay.cola.cloud.framework.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 基于JODA TIME构建日期工具类
 * @author LIQIU
 * @date 2017-12-15
 */
public class DateUtils {

    public static  final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static  final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static  final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static  final String DEFAULT_SHORT_DATE_FORMAT = "yyyyMMdd";


    /**
     * 构建DateTime
     * @param date
     * @return
     */
    private static DateTime getDateTime(Date date){
        return  new DateTime(date);
    }

    /**
     * 构建当前时间的DateTime
     * @return
     */
    private static DateTime getDateTime(){
        return  getDateTime(null);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Date currentDate() {
        return getDateTime().toDate();
    }

    /**
     * 格式化时间
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date,String format){
        return getDateTime(date).toString(format);
    }

    /**
     * 格式化日期格式:yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        return format(date,DEFAULT_DATE_FORMAT);
    }

    /**
     * 格式化日期时间格式:yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatTime(Date date){
        return format(date,DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 格式化时间格式:HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        return format(date,DEFAULT_TIME_FORMAT);
    }


    /**
     * 格式化当前日期
     * @param format
     * @return
     */
    public static String formateCurrent(String format){
        return format(currentDate(),format);
    }

    /**
     * 格式化当前日期为日期
     * @return
     */
    public static String formateShortCurrent(){
        return formateCurrent(DEFAULT_SHORT_DATE_FORMAT);
    }

    /**
     * 格式化当前日期的日期格式
     * @return
     */
    public static String formatDateCurrent(){
        return formatDate(currentDate());
    }

    /**
     * 格式化当前日期的日期时间格式
     * @return
     */
    public static String formatDateTimeCurrent(){
        return formatDateTime(currentDate());
    }

    /**
     * 格式化当前日期的时间格式
     * @return
     */
    public static String formatTimeCurrent(){
        return formatTime(currentDate());
    }

    /**
     * 根据格式解析字符串
     * @param date
     * @param format
     * @return
     */
    public static Date parse(String date,String format){
        return DateTime.parse(date, DateTimeFormat.forPattern(format)).toDate();
    }

    /**
     * 解析成日期格式:yyyy-MM-dd
     * @param date
     * @return
     */
    public static Date parseDate(String date){
        return DateTime.parse(date, DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT)).toDate();
    }

    /**
     * 解析成日期时间格式:yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static Date parseDateTime(String date){
        return DateTime.parse(date, DateTimeFormat.forPattern(DEFAULT_DATE_TIME_FORMAT)).toDate();
    }

    /**
     * 解析成时间格式:HH:mm:ss
     * @param date
     * @return
     */
    public static Date parseTime(String date){
        return DateTime.parse(date, DateTimeFormat.forPattern(DEFAULT_TIME_FORMAT)).toDate();
    }
}

