package com.pingguo.bloomtest.entity.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserRequest {
    private String username;
    private String password;
}
