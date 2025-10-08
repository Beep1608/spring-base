package com.standard.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
            "authority"
    })
})
@IdClass(AuthorityId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authority", length = 50)
    private String authority;


}
