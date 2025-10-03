package com.standard.demo.security.authentication;

import com.standard.demo.security.authentication.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        System.out.println("Rquest :");
        http
        .csrf(csrf ->csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/user/login","/api/user/register").permitAll()
            .anyRequest().authenticated() 
        ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider authProvider) throws Exception{
            
            return new ProviderManager(authProvider);
    }

    //Provider for username/password
    @Bean 
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    //Provider for JWT

    JwtAuthenticationProvider jwtAuthenticationProvider(){

    }

    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }




    
    
}
