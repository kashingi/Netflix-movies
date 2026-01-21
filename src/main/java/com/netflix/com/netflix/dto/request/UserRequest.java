package com.netflix.dto.request;

import lombok.Data;

//Add your annotations here
@Data
public class UserRequest {

    private String email;
    private String fullName;
    private String password;
    private String role;
    private String active;
}
