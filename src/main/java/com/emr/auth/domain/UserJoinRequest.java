package com.emr.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class UserJoinRequest {
    private String userName;
    private String password;

}
