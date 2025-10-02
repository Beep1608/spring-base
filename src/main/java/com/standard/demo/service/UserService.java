package com.standard.demo.service;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.standard.demo.dto.UserDTO;


public interface  UserService extends UserDetailsService {
    
    public Optional<UserDTO> findById(Long id);
    public Optional<UserDTO> findByName(String name);
}
