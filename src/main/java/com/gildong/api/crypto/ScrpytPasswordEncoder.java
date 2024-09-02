package com.gildong.api.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ScrpytPasswordEncoder implements PasswordEncoder{

    private static final SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);


    @Override
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPaasword, String encrpytedPassword){
        return encoder.matches(rawPaasword,encrpytedPassword);
    }
}
