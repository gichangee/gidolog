package com.gildong.api.crypto;

public class PlainPasswordEncoder implements PasswordEncoder{
    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return rawPassword.equals(encryptedPassword);
    }
}
