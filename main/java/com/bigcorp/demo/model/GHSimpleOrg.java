package com.bigcorp.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GHSimpleOrg implements GHSimpleObject {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String  login;

}
