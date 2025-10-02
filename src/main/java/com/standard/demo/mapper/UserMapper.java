package com.standard.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.standard.demo.dto.UserDTO;
import com.standard.demo.entity.User;

@Component
public class UserMapper {

    private ModelMapper modelMapper;

    public UserDTO toDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    
}
