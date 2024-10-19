package com.sumerge.careertrack.career_package_svc.filters;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class TokenAuthentication implements Authentication {

    @Override
    public String getName() {
        return "Custom Sumerge Auth";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_admin");
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Authentication not settable for HeaderAuthentication");
    }

}