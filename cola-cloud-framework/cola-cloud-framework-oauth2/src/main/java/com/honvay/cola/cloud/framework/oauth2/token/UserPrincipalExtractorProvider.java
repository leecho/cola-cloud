package com.honvay.cola.cloud.framework.oauth2.token;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

/**
 * @author LIQIU
 * @date 2018-4-14
 **/
public class UserPrincipalExtractorProvider implements ObjectProvider<PrincipalExtractor> {
    @Override
    public PrincipalExtractor getObject(Object... objects) throws BeansException {
        return new UserPrincipalExtractor();
    }

    @Override
    public PrincipalExtractor getIfAvailable() throws BeansException {
        return new UserPrincipalExtractor();
    }

    @Override
    public PrincipalExtractor getIfUnique() throws BeansException {
        return new UserPrincipalExtractor();
    }

    @Override
    public PrincipalExtractor getObject() throws BeansException {
        return new UserPrincipalExtractor();
    }
}
