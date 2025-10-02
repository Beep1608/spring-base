package com.standard.demo.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.standard.demo.dto.UserDTO;
import com.standard.demo.entity.User;
import com.standard.demo.mapper.UserMapper;
import com.standard.demo.respository.UserRepository;
import com.standard.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    private UserRepository repository;
    private UserMapper mapper;

    @Override
     public Optional<UserDTO> findById(Long id){

        Optional<User> user = repository.findById(id);

        return user.map(mapper::toDto);

     }

       @Override
     public Optional<UserDTO> findByName(String name){
        User user = new User();
        user.setName(name);

        ExampleMatcher matcher  = ExampleMatcher.matching()
        .withIgnorePaths("id");
        Example<User> example = Example.of(user,matcher);

        Optional<User> userEntity = repository.findOne(example);

        return userEntity.map(mapper::toDto);

     }


     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        
        if(findByName(username).isEmpty()){
            throw new UsernameNotFoundException("No encontramos el usuario");
        }
        return  new org.springframework.security.core.userdetails.User(
                username, 
                username, 
                null);
     }
}
