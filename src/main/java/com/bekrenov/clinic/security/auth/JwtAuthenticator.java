package com.bekrenov.clinic.security.auth;

import com.bekrenov.clinic.exception.InvalidAuthHeaderException;
import com.bekrenov.clinic.security.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private static final String BEARER_PREFIX = "Bearer ";

    public void authenticateWithJwt(HttpServletRequest request){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX))
            throw new InvalidAuthHeaderException();
        String jwt = authHeader.substring(BEARER_PREFIX.length());
        if(jwtProvider.validateToken(jwt)){
            String username = jwtProvider.getUsername(jwt);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            var auth = UsernamePasswordAuthenticationToken.authenticated(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else throw new JwtException("Invalid JWT token");
    }
}
