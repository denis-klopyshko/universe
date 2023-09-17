package com.universe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.Map;

@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .failureHandler(failureHandler()))
                .logout(logout -> logout
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                )
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/img/**", "/css/**", "/login*").permitAll()
                        .anyRequest()
                        .authenticated());
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ExceptionMappingAuthenticationFailureHandler failureHandler() {
        var exceptionMapping = Map.of(
                BadCredentialsException.class.getCanonicalName(), "/login?error=BadCredentials",
                DisabledException.class.getCanonicalName(), "/login?error=AccountDisabled"
        );

        var failureHandler = new ExceptionMappingAuthenticationFailureHandler();
        failureHandler.setExceptionMappings(exceptionMapping);
        return failureHandler;
    }
}
