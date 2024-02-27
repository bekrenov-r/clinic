package com.bekrenov.clinic.security.auth.jwt;

import com.bekrenov.clinic.security.Role;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    @Getter
    private String jwt;
    private String principal;

    private static final Function<Claims, Collection<GrantedAuthority>> extractAuthorities = claims -> {
        String[] rolesStr = claims.get("roles", String.class).split(",");
        return Arrays.stream(rolesStr)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    };

    public JwtAuthenticationToken(String jwtToken, JwtProvider jwtProvider){
        super(jwtProvider.getClaimFromToken(jwtToken, extractAuthorities));
        this.principal = jwtProvider.getSubject(jwtToken);
        this.jwt = jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
