//package com.nanum.apigatewayservice.config;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@AllArgsConstructor
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().mvcMatchers(
//                "/error",
//                "/favicon.ico",
//                "/swagger-ui.html",
//                "/swagger/**",
//                "/swagger-resources/**",
//                "/webjars/**",
//                "/v2/api-docs"
//        );
//
//        web.ignoring().antMatchers(
//                "/api/v2//login/**"
//        );
//    }
//}
