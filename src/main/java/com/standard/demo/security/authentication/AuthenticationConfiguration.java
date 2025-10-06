package com.standard.demo.security.authentication;

import com.standard.demo.security.authentication.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${jwt.secret}")
    private String secretKeyString;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        System.out.println("Rquest :");
        http
        .csrf(csrf ->csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/user/login","/api/user/register").permitAll()
            .anyRequest().authenticated() 
        ) //.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //.oauth2ResourceServer(oath2 ->
                //        oath2.jwt(Customizer.withDefaults())
                //)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        var obj = http.build();
        obj.getFilters().stream().forEach(filter -> System.out.println("Filter :"+filter.getClass()));
        return obj;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider authProvider, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception{
            ProviderManager providerManager =  new ProviderManager(authProvider, jwtAuthenticationProvider);
            providerManager.setEraseCredentialsAfterAuthentication(false);
            return providerManager;
    }

    //Provider for username/password
    @Bean 
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(encoder);

        return provider;
    }

    //Provider for JWT

    @Bean
   JwtAuthenticationProvider jwtAuthenticationProvider(JwtDecoder decoder){
      return new JwtAuthenticationProvider(decoder);
   }


    @Bean
    public JwtDecoder jwtDecoder()  {

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }



    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }




    
    
}
