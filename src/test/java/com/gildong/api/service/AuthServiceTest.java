package com.gildong.api.service;

import com.gildong.api.crypto.ScrpytPasswordEncoder;
import com.gildong.api.domain.User;
import com.gildong.api.exception.AlreadyExistsEmailException;
import com.gildong.api.exception.InvalidSigninInformation;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import com.gildong.api.request.Signup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 성공")
    void test1(){

        ScrpytPasswordEncoder encoder = new ScrpytPasswordEncoder();

        Signup signup = Signup.builder()
                .password("1234")
                .email("parkgc0504@naver.com")
                .name("박기창")
                .build();

        authService.signup(signup);

        Assertions.assertEquals(1,userRepository.count());

        User user = userRepository.findAll().iterator().next();
        Assertions.assertEquals("parkgc0504@naver.com",user.getEmail());
//        Assertions.assertNotNull(user.getPassword());
        Assertions.assertTrue(encoder.matches("1234",user.getPassword()));
        Assertions.assertEquals("박기창",user.getName());

    }


    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2(){

        User user = User.builder()
                .email("parkgc0504@naver.com")
                .password("1234")
                .name("기창")
                .build();

        userRepository.save(user);

        Signup signup = Signup.builder()
                .password("1234")
                .email("parkgc0504@naver.com")
                .name("박기창")
                .build();


        Assertions.assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));



    }

    @Test
    @DisplayName("로그인 성공")
    void test3(){

        ScrpytPasswordEncoder encoder = new ScrpytPasswordEncoder();
        String encrpytedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("parkgc0504@naver.com")
                .password(encrpytedPassword)
                .name("기창")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("parkgc0504@naver.com")
                .password("1234")
                .build();

        Long userId = authService.singin(login);

        Assertions.assertNotNull(userId);


    }

    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4(){


        Signup signup = Signup.builder()
                .password("1234")
                .email("parkgc0504@naver.com")
                .name("박기창")
                .build();

        authService.signup(signup);

        Login login = Login.builder()
                .email("parkgc0504@naver.com")
                .password("5678")
                .build();

        Assertions.assertThrows(InvalidSigninInformation.class, () -> authService.singin(login));




    }


}