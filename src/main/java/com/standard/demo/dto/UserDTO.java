package com.standard.demo.dto;

import lombok.Data;

@Data
public class UserDTO {
    
    public Long id;
    public String name;
    public String password;

    public UserDTO(){}
    public UserDTO(Long id,String name, String password){
        this.id= id;
        this.name= name;
        this.password = password;
    }

}