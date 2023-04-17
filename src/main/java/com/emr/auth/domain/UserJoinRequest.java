package com.emr.auth.domain;

import lombok.Getter;

@Getter
public class UserJoinRequest {
    private String userName;
    private String password;
}
