package com.standard.demo.service.impl;

import java.security.Principal;
import java.util.*;

import com.standard.demo.entity.Authority;
import com.standard.demo.respository.AuthorityRepository;
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
   private AuthorityRepository authorityRepository;

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
        user.setUsername(name);

        ExampleMatcher matcher  = ExampleMatcher.matching()
        .withIgnorePaths("id");
        Example<User> example = Example.of(user,matcher);

        Optional<User> userEntity = repository.findOne(example);

        return userEntity.map(mapper::toDto);

     }




	@Override
	public String register(User user){

		user.setPassword(encoder.encode(user.getPassword()));
		user.setEnabled(true);
		User newUser =  repository.save(user);

		Authority authority = new Authority();
		authority.setAuthority("User");
		authority.setUser(newUser);
		authorityRepository.save(authority);


		System.out.println("User credentials crypt: "+ user.getPassword());



		return jwtService.generateToken(newUser.getUsername(),newUser.getPassword());
	}

    @Override
    public String login(Authentication user){

        org.springframework.security.core.userdetails.User userDetail =
                (org.springframework.security.core.userdetails.User) user.getPrincipal();

        return jwtService.generateToken(user.getName(),"1235");
    }


}
