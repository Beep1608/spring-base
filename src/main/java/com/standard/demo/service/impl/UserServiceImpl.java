package com.standard.demo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.standard.demo.service.JwtService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.standard.demo.dto.UserDTO;
import com.standard.demo.entity.User;
import com.standard.demo.mapper.UserMapper;
import com.standard.demo.respository.UserRepository;
import com.standard.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    
   @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder encoder;



    @Autowired
    private JwtService jwtService;

    public  UserServiceImpl(){
      System.out.println("Instanciado UserDetailsService");
    }

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

         System.out.println("USERNAME: "+username);
        var userDto =  findByName(username);
        if(userDto.isEmpty()){
            throw new UsernameNotFoundException("No encontramos el usuario");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("authorized"));


        return  new org.springframework.security.core.userdetails.User(
                userDto.get().name,
                userDto.get().password,
                authorities);
     }

     @Override
     public String register(User user){

        user.setPassword(encoder.encode(user.getPassword()));
        User newUser =  repository.save(user);
        return jwtService.generateToken(newUser.getName(),newUser.getPassword());
     }

    @Override
    public String login(User user){

        return jwtService.generateToken(user.getName(),user.getPassword());
    }

}
