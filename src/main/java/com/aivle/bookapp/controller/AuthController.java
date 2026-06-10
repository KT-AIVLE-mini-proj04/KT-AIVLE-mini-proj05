package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.dto.auth.LoginResponseDto;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.global.util.Sha256Util;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.repository.UserRepository;
import com.aivle.bookapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto user){
        return authService.login(user);
    }
}
