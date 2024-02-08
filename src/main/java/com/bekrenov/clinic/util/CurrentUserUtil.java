package com.bekrenov.clinic.util;

import com.bekrenov.clinic.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {
    private final UserDetailsService userDetailsService;

    private static final Object ANONYMOUS_PRINCIPAL = "anonymousUser";

    public UserDetails getCurrentUser(){
        String username = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetailsService.loadUserByUsername(username);
    }

    public Set<Role> getCurrentUserRoles(){
        UserDetails user = this.getCurrentUser();
        return user.getAuthorities().stream()
                .map(a -> Role.valueOf(a.getAuthority()))
                .collect(Collectors.toSet());
    }

    public boolean hasLoggedUser(){
         Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return !principal.equals(ANONYMOUS_PRINCIPAL);
    }
}
