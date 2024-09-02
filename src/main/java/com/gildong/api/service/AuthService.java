package com.gildong.api.service;

import com.gildong.api.crypto.PasswordEncoder;
import com.gildong.api.domain.User;
import com.gildong.api.exception.AlreadyExistsEmailException;
import com.gildong.api.exception.InvalidSigninInformation;
import com.gildong.api.repository.UserRepository;
import com.gildong.api.request.Login;
import com.gildong.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder scrpytPasswordEncoder;


    @Transactional
    public Long singin(Login login){


        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        boolean matches = scrpytPasswordEncoder.matches(login.getPassword(),user.getPassword());

        if(!matches){
            throw new InvalidSigninInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {

        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if(userOptional.isPresent()){
            throw new AlreadyExistsEmailException();
        }


        String encryptedPassword = scrpytPasswordEncoder.encrypt(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
