package com.bigcorp.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GHSimpleRepo implements GHSimpleObject {

    private static final long serialVersionUID = 1L;

    private Integer           id;

    private String            nodeId;

    private String            name;

    private String            fullName;

    @JsonProperty("private")
    private boolean           isPrivate;

    private GHSimpleUser      owner;

    private String            defaultBranch;

}
