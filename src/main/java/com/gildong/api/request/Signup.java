package com.gildong.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Signup {
    private String name;
    private String password;
    private String email;
}
