package com.nnk.springboot.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Spring Security configuration class for defining authentication and authorization rules.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Provides a password encoder using BCrypt hashing algorithm.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for building MvcRequestMatchers based on Spring MVC path patterns.
     *
     * @param handlerMappingIntrospector used to inspect request mappings
     * @return a builder for creating MvcRequestMatchers
     */
    @Bean
    MvcRequestMatcher.Builder requestMatcher(HandlerMappingIntrospector handlerMappingIntrospector) {
        return new MvcRequestMatcher.Builder(handlerMappingIntrospector);
    }

    /**
     * Defines the security filter chain including access control rules,
     * login, logout behavior, and exception handling.
     *
     * @param http             the HttpSecurity object to configure
     * @param daoAuthProvider  the authentication provider (DAO-based)
     * @param mvc              the MvcRequestMatcher builder
     * @return a configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider daoAuthProvider,
                                           MvcRequestMatcher.Builder mvc) throws Exception {
        return http
                // Allow internal dispatch types
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/app/login").permitAll()
                        .requestMatchers("/app/error").permitAll()
                        .requestMatchers(mvc.pattern("/user")).hasRole("ADMIN")
                        .requestMatchers(mvc.pattern("/user/**")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // Configure custom login page
                .formLogin(formLogin -> formLogin
                        .loginPage("/app/login")
                        .defaultSuccessUrl("/bidList/list", true)
                        .permitAll()
                )
                // Configure logout handling
                .logout(logout -> logout
                        .logoutUrl("/app-logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                // Handle access denied errors
                .exceptionHandling(exc -> exc
                        .accessDeniedPage("/app/error")
                )
                // Set the authentication provider
                .authenticationProvider(daoAuthProvider)
                .build();
    }

    /**
     * Configures the AuthenticationManager with the custom user details service and password encoder.
     *
     * @param http                  the HttpSecurity object to retrieve shared objects
     * @param bCryptPasswordEncoder the password encoder to use
     * @return a configured AuthenticationManager
     * @throws Exception in case of configuration errors
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Provides a DAO-based authentication provider using the custom user details service
     * and the BCrypt password encoder.
     *
     * @return a configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}