package com.honvay.cola.cloud.auth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.auth.client.entity.Client;
import com.honvay.cola.cloud.auth.client.entity.Scope;
import com.honvay.cola.cloud.auth.client.service.ClientService;
import com.honvay.cola.cloud.auth.client.service.ScopeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DatabaseCachableClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    private static final String OAUTH_CLINET_DETAILS_CACHE = "oauth_client_details";

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScopeService scopeService;

    @Override
    @Cacheable(value = OAUTH_CLINET_DETAILS_CACHE ,key = "#clientId")
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<Client> clientOptional = Optional.of(this.clientService.findOneByColumn("client_id", clientId));
        return clientOptional.map(entityToDomain).<ClientRegistrationException>orElseThrow(() -> new NoSuchClientException("Client ID not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = OAUTH_CLINET_DETAILS_CACHE ,key = "#clientDetails.clientId")
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        if (this.clientService.findOneByColumn("client_id", clientDetails.getClientId()) != null) {
            throw new ClientAlreadyExistsException("Client ID already exists");
        }

        Client client = Client.builder()
                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds()).build();

        client.setGrantType(clientDetails.getAuthorizedGrantTypes().stream().collect(Collectors.joining(",")));
        client.setRedirectUri(clientDetails.getRegisteredRedirectUri().stream().collect(Collectors.joining(",")));


        clientService.insert(client);

        List<Scope> clientScopes = clientDetails.getScope().stream().map(scope ->
                Scope.builder().clientId(client.getId()).autoApprove(false).scope(scope).build()).collect(Collectors.toList());

        this.scopeService.insertBatch(clientScopes);

    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = OAUTH_CLINET_DETAILS_CACHE ,key = "#clientDetails.clientId")
    public ClientDetails updateCachedClientDetail(ClientDetails clientDetails) throws NoSuchClientException{
        Optional<Client> clientOptional = Optional.of(this.clientService.findOneByColumn("client_id", clientDetails.getClientId()));
        clientOptional.orElseThrow(() -> new NoSuchClientException("Client ID not found"));

        Client client = Client.builder()
                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds()).build();
        client.setId(clientOptional.get().getId());

        client.setGrantType(clientDetails.getAuthorizedGrantTypes().stream().collect(Collectors.joining(",")));
        client.setRedirectUri(clientDetails.getRegisteredRedirectUri().stream().collect(Collectors.joining(",")));
        clientService.insert(client);

        EntityWrapper<Scope> wrapper = new EntityWrapper<>();
        wrapper.eq("client_id", clientOptional.get().getId());
        scopeService.delete(wrapper);

        List<Scope> clientScopes = clientDetails.getScope().stream().map(scope ->
                Scope.builder().clientId(client.getId()).autoApprove(false).scope(scope).build()).collect(Collectors.toList());
        this.scopeService.insertBatch(clientScopes);
        return clientDetails;
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        this.updateCachedClientDetail(clientDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        Optional<Client> clientOptional = Optional.of(this.clientService.findOneByColumn("client_id", clientId));
        clientOptional.orElseThrow(() -> new NoSuchClientException("Client ID not found"));
        clientOptional.get().setClientSecret(passwordEncoder.encode(secret));
        this.clientService.updateById(clientOptional.get());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = OAUTH_CLINET_DETAILS_CACHE ,key = "#clientId")
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        EntityWrapper<Client> wrapper = new EntityWrapper<>();
        wrapper.eq("client_id", clientId);
        clientService.delete(wrapper);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return clientService.selectList().stream().filter(Client::getEnable).map(entityToDomain).collect(Collectors.toList());
    }

    private final Function<? super Client, ? extends BaseClientDetails> entityToDomain = entity -> {
        BaseClientDetails clientDetails = new BaseClientDetails();

        clientDetails.setClientId(entity.getClientId());
        clientDetails.setClientSecret(entity.getClientSecret());

        clientDetails.setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());

        clientDetails.setAuthorizedGrantTypes(Arrays.asList(entity.getGrantType().split(",")));

        List<Scope> clientScopes = this.scopeService.selectListByColumn("client_id",entity.getId());

        clientDetails.setScope(clientScopes.stream().map(clientScope -> clientScope.getScope()).collect(Collectors.toList()));

        clientDetails.setAutoApproveScopes(clientScopes.stream().filter(Scope::getAutoApprove).map(clientScope -> clientScope.getScope()).collect(Collectors.toList()));

        if(StringUtils.isNotEmpty(entity.getResourceIds())){
            clientDetails.setResourceIds(Arrays.stream(StringUtils.split(entity.getResourceIds(), ",")).collect(Collectors.toList()));
        }
        if(StringUtils.isNotEmpty(entity.getResourceIds())){
            clientDetails.setRegisteredRedirectUri(Arrays.stream(StringUtils.split(entity.getRedirectUri(), ",")).collect(Collectors.toSet()));
        }

        clientDetails.setAdditionalInformation(Collections.<String, Object>emptyMap());

        return clientDetails;
    };

}
