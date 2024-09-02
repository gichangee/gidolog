package com.gildong.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildong.api.domain.User;
import com.gildong.api.repository.SessionRepository;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.hodolman.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerDocTest {

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
    @DisplayName("로그인 테스트")
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
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("login",   PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email").description("사용자 이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("사용자 비밀번호")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("accessToken").description("토큰")
                        )
                ));


    }



}