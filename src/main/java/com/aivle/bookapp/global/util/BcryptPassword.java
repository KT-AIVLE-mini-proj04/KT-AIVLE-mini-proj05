package com.aivle.bookapp.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class BcryptPassword {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();

    public static String encrypt(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean matches(String password, String hash) {
        return bCryptPasswordEncoder.matches(password, hash);
    }
}
