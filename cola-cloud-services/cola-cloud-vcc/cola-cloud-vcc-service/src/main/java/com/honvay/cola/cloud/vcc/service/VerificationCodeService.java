package com.honvay.cola.cloud.vcc.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 验证码服务接口
 * @author LIQIU
 * @date 2018-1-8
 */
public interface VerificationCodeService {

    /**
     * 验证码缓存名称
     */
    String VERIFICATION_CODE_CACHE_NAME = "verificationCodeCache";
    /**
     * 验证码短信发送记录缓存名称
     */
    String VERIFICATION_CODE_SMS_SEND_CACHE_NAME = "verificationCodeSmsSendCache";

    /**
     * 验证码和手机号缓存
     */
    String VERIFICATION_CODE_PHONE_NUMBER_CACHE_NAME = "verificationPhoneNumberCodeCache";

    /**
     * 获取验证码token
     * @param size  验证码长度
     * @param expire 过期时间
     * @param type 类型 0：数字验证码 1：混合验证码
     * @return
     * 验证码长度和过期时间采用默认配置
     * 验证码长度配置项：cola.vc.size 默认值：4<br/>
     * 验证码过期时间配置项：cola.vc.expire 默认值：180000(3分钟)
     */
    String getToken(Integer size, Long expire, String type, String subject);

    /**
     * 刷新验证码有效期
     * 验证码过期时间配置项：cola.vc.expire 默认值：180000(3分钟)
     * @param token 验证码token
     */
    void refresh(String token);

    /**
     * 刷新验证码
     * @param token 验证码token
     * @param expire 验证码过期时间
     */
    void refresh(String token, long expire);

    /**
     * 发送短信验证码
     * @param token 验证码TOKEN
     * @param phoneNumber 手机号码
     * 短信模板配置项：cola.vc.sms.templateCode<br/>
     * 短信签名配置项：cola.vc.sms.signName<br/>
     * 验证码参数名配置项：cola.vc.sms.codeParamName<br/>
     * 可以对上述三个配置项进行配置全局的发送短信参数
     */
    void sendSms(String token, String phoneNumber);

    /**
     * 发送短信验证码
     * @param token 验证码TOKEN
     * @param templateCode 短信模板
     * @param signName 短信签名
     * @param phoneNumber 手机号码
     * @param codeParamName 短信验证码参数名
     * @param params 短信参数
     */
    void sendSms(String token, String templateCode, String signName, String phoneNumber, String codeParamName, Map<String, Object> params);
/*
    String getToken(int size, String type);

    *//**
     * 获取验证码
     * @param type
     * @return
     *//*
    String getToken(String type);

    *//**
     * 获取验证码token
     * @param size 验证码长度
     * @return
     *//*
    String getToken(int size);

    *//**
     * 获取验证码token
     * @return
     * 验证码长度和过期时间采用默认配置
     * 验证码长度配置项：cola.vc.size 默认值：4<br/>
     * 验证码过期时间配置项：cola.vc.expire 默认值：180000(3分钟)
     *//*
    String getToken();*/

    /**
     * 渲染验证码图片
     * @param token
     * @param outputStream
     * @throws IOException
     */
    void renderImage(String token, OutputStream outputStream) throws IOException;

    /**
     * 判断验证码是否匹配，默认忽略大小写
     * @param token 验证码TOKEN
     * @param code 验证码值
     * @return
     */
    boolean validate(String token, String code, String phoneNumber);

    /**
     * 判断验证是否匹配
     * @param token 验证码TOKEN
     * @param code 验证码值
     * @param ignoreCase 忽略大小写
     * @return
     */
    boolean validate(String token, String code, String phoneNumber, boolean ignoreCase);
}
