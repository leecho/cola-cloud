package com.honvay.cola.cloud.auth.integration.authenticator;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.uc.model.SysUserDO;

/**
 * @author LIQIU
 * @date 2018-4-4
 **/
public abstract class AbstractPreparableIntegrationAuthenticator implements IntegrationAuthenticator{

    @Override
    public abstract SysUserDO authenticate(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract void prepare(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract boolean support(IntegrationAuthentication integrationAuthentication);

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
