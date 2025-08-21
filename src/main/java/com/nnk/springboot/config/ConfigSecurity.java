package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfigSecurity {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .formLogin(form -> form.disable())
//                .headers(headers -> headers
//                        .cacheControl(cache -> {})
//                );
//
//        return http.build();
//    }
}
