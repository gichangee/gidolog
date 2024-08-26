package com.gildong.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Login {
    
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
