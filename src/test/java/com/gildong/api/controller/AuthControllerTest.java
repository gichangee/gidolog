package com.gildong.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildong.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
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