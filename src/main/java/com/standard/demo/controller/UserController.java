package com.standard.demo.controller;

import com.standard.demo.entity.User;
import com.standard.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.standard.demo.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private  UserService service = new UserServiceImpl();


	@GetMapping("/login")
	public ResponseEntity<String> login(Authentication user){

		System.out.println("Intentando loggear user");
		return new ResponseEntity<String>("Token: "+service.login(user), HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user){
		System.out.println("MIAAAU");
		String jwtToken = service.register(user);
		return  ResponseEntity.ok(jwtToken);
	}

}
