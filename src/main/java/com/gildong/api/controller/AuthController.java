package com.gildong.api.controller;

import com.gildong.api.config.AppConfig;
import com.gildong.api.request.Login;
import com.gildong.api.request.Signup;
import com.gildong.api.response.SessionResponse;
import com.gildong.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody @Valid Login login){

        Long userId = authService.singin(login);


        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(key)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .compact();


        return new SessionResponse(jws);
    }


    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        authService.signup(signup);
    }

}
