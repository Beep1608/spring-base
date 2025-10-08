package com.standard.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password", length = 500)
    private String password;

    private boolean enabled;

    //@OneToMany(mappedBy = "user")
    //private Collection<Authority> authorities = new HashSet<>();




}
