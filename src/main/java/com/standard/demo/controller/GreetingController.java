package com.standard.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.SecurityMarker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    
    @GetMapping("/")
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Access secured enpoint", security = @SecurityRequirement(name = "BearerAuth"))
    public String greeting(){
        return "Hola Chetos";
    }
    @GetMapping("/api/miau")
    public String miau(){
        return "Miau";
    }
}
