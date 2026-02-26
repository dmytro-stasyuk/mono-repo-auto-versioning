package com.example.monorepo.security;

import com.example.monorepo.utils.StringUtils;

public class SecurityContext {

    private String principal;

    public boolean isAuthenticated() {
        return !StringUtils.isBlank(principal);
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
