package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.dto.auth.LoginResponseDto;
import com.aivle.bookapp.global.util.BcryptPassword;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDto login(LoginRequestDto user) {
        String email = user.getEmail();
        String passwordRaw = user.getPassword();

        String passwordHash = BcryptPassword.encrypt(passwordRaw);

        User existedUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("로그인을 실패했습니다."));
        if (!existedUser.getPassword().equals(passwordHash)) {
            throw new RuntimeException("로그인을 실패했습니다.");
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
