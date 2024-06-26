package com.bekrenov.clinic.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentAuthUtil {
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean hasAuthority(GrantedAuthority authority){
        return getAuthentication().getAuthorities().contains(authority);
    }

    public static boolean isAuthenticated(){
        Authentication auth = getAuthentication();
        AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        return auth != null && !trustResolver.isAnonymous(auth);
    }
}
