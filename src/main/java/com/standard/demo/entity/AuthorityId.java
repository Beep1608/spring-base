package com.standard.demo.entity;

import java.io.Serializable;

public class AuthorityId implements Serializable {

    private String user;
    private String authority;

    public AuthorityId() {}

    public AuthorityId(String user, String authority) {
        this.user = user;
        this.authority = authority;
    }

}
