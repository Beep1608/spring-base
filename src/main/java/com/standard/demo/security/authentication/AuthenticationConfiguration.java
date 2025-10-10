package com.standard.demo.security.authentication;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.crypto.SecretKey;
import javax.sql.DataSource;
import java.util.List;


@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {


	@Value("${jwt.secret}")
	private String secretKeyString;




	//BasicAuth Chain

	@Bean
	public SecurityFilterChain basiAuthChain(HttpSecurity http) throws Exception{

		http
				//.csrf((csrf) -> csrf.disable())
				.csrf(csrf ->{
					csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
							.ignoringRequestMatchers("/users/register");
				})
				.securityMatcher("/users/login","/users/register")
				.authorizeHttpRequests(auth -> auth
						.anyRequest().permitAll()
				)
				.httpBasic(Customizer.withDefaults());

		var obj = http.build();
		obj.getFilters().stream().forEach(filter -> System.out.println("Filter :"+filter.getClass()));
		return obj;
	}

	//JWT Chain

	@Bean
	public SecurityFilterChain jwtChain(HttpSecurity http) throws Exception{

		http
				.securityMatcher("/api/**")
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer((oauth2) -> oauth2
						.jwt(Customizer.withDefaults()));

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
