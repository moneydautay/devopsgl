package com.greenlucky.backend.persistence.domain.backend;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Mr Lam on 6/11/2016.
 */
public class Authority implements GrantedAuthority{

    private final String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
