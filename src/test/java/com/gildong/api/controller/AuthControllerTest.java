package com.gildong.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildong.api.domain.Session;
import com.gildong.api.domain.User;
import com.gildong.api.repository.SessionRepository;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import com.gildong.api.request.Signup;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        //given
        userRepository.save(User.builder()
                .name("기창")
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build());


        Login login = Login.builder()
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(jsonPath("$.code").value("400"))
//            .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
//            .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test2() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("기창")
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build());

        Login login = Login.builder()
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


        Assertions.assertEquals(1L, user.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("기창")
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build());

        Login login = Login.builder()
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
//            .andExpect(MockMvcResultMatchers.header().exists("Set-Cookie"))  // Set-Cookie 헤더가 있는지 확인
//            .andExpect(MockMvcResultMatchers.cookie().exists("SESSION"))     // SESSION 쿠키가 있는지 확인
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test4() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("기창")
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build());

        Session session = user.addSession();
        userRepository.save(user);


        // JWT 토큰 생성
        String secretKey = "H9xWQ2HuOWfU6HB08WaJRXmBP9A1cuRUwI577GHRpCE";
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(decodedKey);


        String jws = Jwts.builder()
                .subject(String.valueOf(user.getId())) // JWT의 subject에 사용자 ID 설정
                .signWith(key) // 비밀 키로 서명
                .compact();

        // Cookie cookie = new Cookie("SESSION", session.getAccessToken());

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        //.cookie(cookie) // 쿠키를 요청에 포함
                        .header("Authorization", jws)
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다")
    void test5() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("기창")
                .email("parkgc0504@gmail.com")
                .password("dpqls")
                .build());

        Session session = user.addSession();
        userRepository.save(user);

        Cookie cookie = new Cookie("SESSION", session.getAccessToken() + "-other");


        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        //.header("Authorization",session.getAccessToken()+"-other")
                        .cookie(cookie) // 쿠키를 요청에 포함
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        //given

        Signup signup = Signup.builder()
                .password("1234")
                .email("parkgc0504@naver.com")
                .name("박기창")
                .build();


        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")

                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }



}