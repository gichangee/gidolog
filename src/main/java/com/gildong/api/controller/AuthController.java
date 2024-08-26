package com.gildong.api.controller;

import com.gildong.api.domain.User;
import com.gildong.api.exception.InvalidSigninInformation;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody @Valid Login login){
        //json 아이디/비밀번호
        log.info(">>>login={}",login);
        //DB에서 조회

        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(()-> new InvalidSigninInformation());

        return user;

        //토큰을 응답
    }

}
