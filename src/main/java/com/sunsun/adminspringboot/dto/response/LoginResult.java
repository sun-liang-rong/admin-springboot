package com.sunsun.adminspringboot.dto.response;

import lombok.Data;

@Data
public class LoginResult {
    private String token;
    private Integer userId;
    private String name;
    private int age;
    private String email;
}
