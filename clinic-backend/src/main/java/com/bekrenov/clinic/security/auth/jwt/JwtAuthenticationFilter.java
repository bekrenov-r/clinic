package com.bekrenov.clinic.security.auth.jwt;

import com.bekrenov.clinic.exception.InvalidAuthHeaderException;
import com.bekrenov.clinic.util.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${spring.custom.security.request-matchers.permit-all}")
    private String[] permitAllMatchers;

    @Value("${spring.custom.security.request-matchers.optional-auth}")
    private String[] optionalAuthMatchers;

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = extractAuthToken(request);
        JwtAuthenticationToken auth = new JwtAuthenticationToken(authToken, jwtProvider);
        authenticationManager.authenticate(auth);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        boolean isOptionalAuthRequest = Arrays.stream(this.optionalAuthMatchers)
                .map(this::parseMatcher)
                .anyMatch(m -> m.matches(request));
        if(isOptionalAuthRequest && !RequestUtils.requestHasHeader(HttpHeaders.AUTHORIZATION)){
            return true;
        }

        return Arrays.stream(permitAllMatchers)
                .map(this::parseMatcher)
                .anyMatch(matcher -> matcher.matches(request));
    }

    private AntPathRequestMatcher parseMatcher(String matcher){
        String[] arr = matcher.split(" ");
        return new AntPathRequestMatcher(arr[1], arr[0]);
    }

    private String extractAuthToken(HttpServletRequest request){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX))
            throw new InvalidAuthHeaderException();
        return authHeader.substring(BEARER_PREFIX.length());
    }
}
