package com.standard.demo.security.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import com.standard.demo.service.impl.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
        .csrf(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated() 
        )
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(){
        return new UserServiceImpl();
    }
    
    
}
