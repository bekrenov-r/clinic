package com.bekrenov.clinic.config;

import com.bekrenov.clinic.service.ClinicUserDetailsService;
import com.bekrenov.clinic.service.ClinicUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    // todo: move jpa config to another config class
    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }

    // todo: make ClinicUserDetailsService @Service, not bean
    @Bean
    public ClinicUserDetailsService userDetailsService(JdbcUserDetailsManager jdbcUserDetailsManager,
                                                       AuthenticationManager authenticationManager,
                                                       PasswordEncoder passwordEncoder){
        return new ClinicUserDetailsServiceImpl(jdbcUserDetailsManager, authenticationManager, passwordEncoder);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(JdbcUserDetailsManager jdbcUserDetailsManager){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        // todo: replace jdbcUserDetailsManager call with something else (some other implementation of UserDetailsService)
        provider.setUserDetailsService(username -> jdbcUserDetailsManager.loadUserByUsername(username));
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider){
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/registration").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/addresses/cities/**").permitAll()
                                .requestMatchers("/patient/create-profile").permitAll()
                                .requestMatchers("/patients/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticate")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll());
        return http.build();
    }
}
