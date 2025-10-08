package com.standard.demo.entity;

import java.io.Serializable;

public class AuthorityId implements Serializable {

    private Long id;
    private String  authority;

    public AuthorityId() {}

    public AuthorityId(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

}
