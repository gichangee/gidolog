package com.gildong.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildong.api.domain.Session;
import com.gildong.api.domain.User;
import com.gildong.api.repository.SessionRepository;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import com.gildong.api.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


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
    void clean(){
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



        Assertions.assertEquals(1L,user.getSessions().size());
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
            .andDo(MockMvcResultHandlers.print());



        Assertions.assertEquals(1L,user.getSessions().size());
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



        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                .header("Authorization",session.getAccessToken())
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



        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                .header("Authorization",session.getAccessToken()+"-other")
                .contentType(MediaType.APPLICATION_JSON)
            )

            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andDo(MockMvcResultHandlers.print());

    }

//
//    @Test
//    @DisplayName("로그인 시 해당 아이디와 비밀번호가 DB에 있는지 확인")
//    void test() throws Exception {
//        // Given
//        User user = new User();
//        user.setName("Test User");
//        user.setEmail("test@example.com");
//        user.setPassword("password123");
//        userRepository.save(user);
//
//        Login login = new Login();
//        login.setEmail("test@example.com");
//        login.setPassword("password123");
//
//        String loginRequestJson = objectMapper.writeValueAsString(login);
//
//        // When
//        ResultActions resultActions = mockMvc.perform(post("/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(loginRequestJson));
//
//        // Then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//
//        assertThat(userRepository.findByEmailAndPassword("test@example.com", "password123")).isPresent();
//    }
//
//    @Test
//    @DisplayName("로그인 시 잘못된 아이디 또는 비밀번호로 인한 실패 확인")
//    void testInvalidLogin() throws Exception {
//        // Given
//        User user = new User();
//        user.setName("Test User");
//        user.setEmail("test@example.com");
//        user.setPassword("password123");
//        userRepository.save(user);
//
//        Login login = new Login();
//        login.setEmail("test@example.com");
//        login.setPassword("wrongpassword");
//
//        String loginRequestJson = objectMapper.writeValueAsString(login);
//
//        // When
//        ResultActions resultActions = mockMvc.perform(post("/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(loginRequestJson));
//
//        // Then
//        resultActions.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("아이디/비밀번호가 올바르지 않습니다."));
//    }

}