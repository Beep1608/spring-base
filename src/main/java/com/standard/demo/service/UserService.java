package com.standard.demo.service;

import java.util.Optional;

import com.standard.demo.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.standard.demo.dto.UserDTO;


public interface  UserService extends UserDetailsService {
    
    public Optional<UserDTO> findById(Long id);
    public Optional<UserDTO> findByName(String name);

    String register(User user);
    String login(Authentication user);
}
