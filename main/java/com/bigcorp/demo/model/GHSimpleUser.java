package com.bigcorp.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GHSimpleUser implements GHSimpleObject {

    private static final long serialVersionUID = 1L;

    private Integer           id;

    private String            login;

    private AccountType       type;

    private String            url;

    private String            htmlUrl;

    public enum AccountType {
        USER, ORGANIZATION;
    }
}
