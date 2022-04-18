package com.bigcorp.demo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GHSimpleRepoEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            action;

    private GHSimpleRepo      repository;

    private GHSimpleOrg       organization;

}
