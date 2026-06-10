package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.dto.auth.LoginResponseDto;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.global.util.Sha256Util;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto user){
        String email = user.getEmail();
        String passwordRaw = user.getPassword();

        String passwordHash = Sha256Util.encrypt(passwordRaw);

        User existedUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        if (existedUser.getPassword().equals(passwordHash)) {
            throw new RuntimeException("비밀번호를 틀렸습니다.");
        }

        String refreshToken = jwtTokenProvider.createRefreshToken(email);
        Token token = new Token();
        token.setUser(existedUser);
        token.setToken(refreshToken);
        tokenRepository.save(token);

        String accessToken = jwtTokenProvider.createAccessToken(email);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setEmail(existedUser.getEmail());
        loginResponseDto.setName(existedUser.getName());
        loginResponseDto.setPhone(existedUser.getPhoneNumber());
        loginResponseDto.setAddress(existedUser.getAddress());

        return loginResponseDto;
    }
}
