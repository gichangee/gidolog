package com.gildong.api.controller;

import com.gildong.api.domain.User;
import com.gildong.api.exception.InvalidSigninInformation;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import com.gildong.api.response.SessionResponse;
import com.gildong.api.service.AuthService;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login login){

        String accessToken = authService.singin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken) //생성된 액세스 토큰을 SESSION이라는 이름의 쿠키로 만듭니다.
            .domain("localhost") // 쿠키가 적용될 도메인을 설정 todo 서버 환경에 따른 분리 필요
            .path("/")//쿠키가 유효한 경로를 설정
            .maxAge(Duration.ofDays(30)) //쿠키의 유효기간을 30일로 설정
            .httpOnly(true) // 쿠키가 HTTP 전용으로 설정되며, 자바스크립트에서 접근할 수 없도록 합니다.
            .secure(false) //  HTTPS에서만 쿠키가 전송되도록 하는 옵션
            .sameSite("Strict") //쿠키의 SameSite 속성을 설정하여 크로스 사이트 요청에서 쿠키가 전송되지 않도록 함
            .build();

        log.info(">>>>>>>>>>>>> cookie={}",cookie);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .build();
    }

}
