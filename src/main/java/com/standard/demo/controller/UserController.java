package com.standard.demo.controller;

import com.standard.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.standard.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService service;


    @GetMapping("/login")
    public ResponseEntity<String> login(Authentication user){

        return new ResponseEntity<String>("Token: "+service.login(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){

        String jwtToken = service.register(user);
        return  ResponseEntity.ok(jwtToken);
    }

}
