package com.standard.demo;


import org.mockito.Mockito;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

	@Bean
	@Primary
	JwtDecoder testJwtDeecoder(){
		return Mockito.mock(JwtDecoder.class);
	}
}
