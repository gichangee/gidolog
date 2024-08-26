package com.gildong.api.service;

import com.gildong.api.domain.Session;
import com.gildong.api.domain.User;
import com.gildong.api.exception.InvalidSigninInformation;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public String singin(Login login){

        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
            .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();
        return session.getAccessToken();
    }
}
