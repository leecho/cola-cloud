package com.honvay.cola.cloud.auth.sms;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.framework.util.BeanUtils;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.vcc.client.VccClient;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lengleng
 * @date 2018/1/9
 * 手机号登录验证filter
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/app/token", "POST"));
    }

    private com.honvay.cola.cloud.uc.client.UcClient ucClient;

    private VccClient vccClient;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String phoneNumber = obtainMobile(request);

        if(StringUtils.isEmpty(phoneNumber)){
            throw new AuthenticationServiceException(
                    "The phoneNumber must not be null");
        }

        phoneNumber = phoneNumber.trim();

        String token = request.getParameter("token");
        String code = request.getParameter("code");

        Assert.isTrue(StringUtils.isNotEmpty(token),"登录令牌缺失");
        Assert.isTrue(StringUtils.isNotEmpty(code),"验证码缺失");

        /*Result<Boolean> result = vccClient.validate(token,code,phoneNumber);
        if(!result.getData()){
            throw new BadCredentialsException("验证码错误");
        }*/

        //先判断手机号是否有注册过
        com.honvay.cola.cloud.uc.model.UserVO userVO = ucClient.findUserByPhoneNumber(phoneNumber);
        if(userVO == null){
            //没有注册则先进行注册
            Result registerResult = ucClient.register(phoneNumber,request.getParameter("client"));
            if(!registerResult.getSuccess()){
                throw new BadCredentialsException(registerResult.getMsg());
            }
        }

        User user = new User();
        BeanUtils.copyProperties(userVO,user);
        SmsAuthenticationToken mobileAuthenticationToken = new SmsAuthenticationToken(phoneNumber);

        setDetails(request, mobileAuthenticationToken);

        return this.getAuthenticationManager().authenticate(mobileAuthenticationToken);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter("phoneNumber");
    }

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public com.honvay.cola.cloud.uc.client.UcClient getUcClient() {
        return ucClient;
    }

    public void setUcClient(com.honvay.cola.cloud.uc.client.UcClient ucClient) {
        this.ucClient = ucClient;
    }

    public VccClient getVccClient() {
        return vccClient;
    }

    public void setVccClient(VccClient vccClient) {
        this.vccClient = vccClient;
    }
}

