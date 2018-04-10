package com.honvay.cola.cloud.auth.integration.authenticator.webchat.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.client.UcClientConstant;
import com.honvay.cola.cloud.uc.model.UserVO;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 小程序集成认证
 *
 * @author LIQIU
 * @date 2018-3-31
 **/
@Service
public class MiniAppIntegrationAuthenticator implements IntegrationAuthenticator {

    @Autowired
    private UcClient ucClient;

    @Autowired
    private WxMaService wxMaService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserVO authenticate(IntegrationAuthentication integrationAuthentication) {
        WxMaJscode2SessionResult session = null;
        String password = integrationAuthentication.getAuthParameter("password");
        try {
            session = this.wxMaService.getUserService().getSessionInfo(password);
            WechatMiniAppToken wechatToken = new WechatMiniAppToken(session.getOpenid(), session.getUnionid(), session.getSessionKey());
            // 加密算法的初始向量
            wechatToken.setIv(integrationAuthentication.getAuthParameter("iv"));
            // 用户的加密数据
            wechatToken.setEncryptedData(integrationAuthentication.getAuthParameter("encryptedData"));
        } catch (WxErrorException e) {
            throw new InternalAuthenticationServiceException("获取微信小程序用户信息失败",e);
        }
        String openId = session.getOpenid();
        UserVO userVO = ucClient.findUserBySocial(UcClientConstant.SOCIAL_TYPE_WECHAT_MINIAP, openId);
        if(userVO != null){
            userVO.setPassword(passwordEncoder.encode(password));
        }
        return userVO;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return UcClientConstant.SOCIAL_TYPE_WECHAT_MINIAP.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
