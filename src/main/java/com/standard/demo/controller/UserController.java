package com.standard.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.standard.demo.dto.UserDTO;
import com.standard.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService service;


    @GetMapping("/login")
    public ResponseEntity<String> getUser(){

        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        if(auth == null){
            return new ResponseEntity<>("No existe usuario", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Usuario: "+auth.getName(), HttpStatus.OK);
    }

}
