package com.standard.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    
    @GetMapping("/")
	@PreAuthorize("isAuthenticated()")
    public String greeting(){
        return "Hola Chetos";
    }
    @GetMapping("/api/miau")
    public String miau(){
        return "Miau";
    }
}
