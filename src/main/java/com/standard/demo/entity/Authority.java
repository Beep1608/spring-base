package com.standard.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
            "username","authority"
    })
})
@IdClass(AuthorityId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @JoinColumn(name = "username",referencedColumnName = "username")
    @ManyToOne
    private User user;

    @Id
    @Column(name = "authority", length = 50)
    private String authority;


}
