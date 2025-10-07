package com.standard.demo.security.authentication;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {


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
        )
             //  .securityContext(securityContex ->{
             //      securityContex.securityContextRepository(new HttpSessionSecurityContextRepository());
             //  })
            .oauth2ResourceServer((oauth2) -> oauth2
                 .jwt(Customizer.withDefaults()))
                //.formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        var obj = http.build();
        obj.getFilters().stream().forEach(filter -> System.out.println("Filter :"+filter.getClass()));
        return obj;
    }


    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public AuthenticationManager authenticationManager( DaoAuthenticationProvider daoAuthenticationProvider,JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception{
            ProviderManager providerManager =  new ProviderManager( daoAuthenticationProvider,jwtAuthenticationProvider);
            return providerManager;
    }

    //Provider for username/password
     @Bean
     public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder){
         System.out.println("UserDetailService : " + userDetailsService.getClass());
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

        SecretKey key  = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }



    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }




    
    
}
