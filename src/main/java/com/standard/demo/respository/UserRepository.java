package com.standard.demo.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.standard.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
