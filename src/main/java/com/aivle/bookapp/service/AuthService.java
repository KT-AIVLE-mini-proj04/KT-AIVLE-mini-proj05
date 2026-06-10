package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.dto.auth.LoginResponseDto;
import com.aivle.bookapp.global.util.BcryptPassword;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDto login(LoginRequestDto user) {
        String loginId = user.getLoginId();
        String passwordRaw = user.getPassword();

        Users existedUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("로그인을 실패했습니다."));
        if (!BcryptPassword.matches(passwordRaw, existedUser.getPassword())) {
            throw new RuntimeException("로그인을 실패했습니다.");
        }

        String refreshToken = jwtTokenProvider.createRefreshToken(loginId);
        Token token = new Token();
        token.setUser(existedUser);
        token.setToken(refreshToken);
        tokenRepository.save(token);

        String accessToken = jwtTokenProvider.createAccessToken(loginId);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setUsersId(existedUser.getUsersId());
        loginResponseDto.setLoginId(existedUser.getLoginId());
        loginResponseDto.setName(existedUser.getName());
        loginResponseDto.setGubun(existedUser.getGubun());
        loginResponseDto.setEmail(existedUser.getEmail());
        loginResponseDto.setAddress(existedUser.getAddress());
        loginResponseDto.setPhoneNumber(existedUser.getPhoneNumber());

        return loginResponseDto;
    }

}
