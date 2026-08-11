package com.sunsun.adminspringboot.dto.request.req;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequest {
    private String name;
    private String password;
}
