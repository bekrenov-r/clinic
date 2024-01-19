package com.bekrenov.clinic.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterChainExceptionHandlerFilter extends OncePerRequestFilter {
    private final ClinicResponseEntityExceptionHandler exceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex){
            this.handleAuthenticationException(ex, response);
        } catch (JwtException ex){
            this.handleJwtException(ex, response);
        }
    }

    private void handleAuthenticationException(AuthenticationException ex, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = exceptionHandler.handleAuthenticationException(ex).getBody();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(this.convertErrorResponseToString(errorResponse));
    }

    private void handleJwtException(JwtException ex, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = exceptionHandler.handleJwtException(ex).getBody();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(this.convertErrorResponseToString(errorResponse));
    }

    private String convertErrorResponseToString(ErrorResponse errorResponse) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(errorResponse);
    }
}
