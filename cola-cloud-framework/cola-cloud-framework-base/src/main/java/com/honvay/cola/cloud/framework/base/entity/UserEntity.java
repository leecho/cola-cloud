package com.honvay.cola.cloud.framework.base.entity;

import java.io.Serializable;

public interface UserEntity extends Serializable {

    Long getId();

    void setId(Long id);

    String getUsername();

    void setUsername(String loginName);

    String getName();

    void setName(String fullName);

    String getPassword();

    void setPassword(String password);

    String getSalt();

    void setSalt(String salt);

    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

  /*  String getToken();

    void setToken(String token);*/
}
