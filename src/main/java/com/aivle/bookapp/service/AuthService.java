package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(UNAUTHORIZED, "리프레시 토큰 쿠키가 없습니다.");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(UNAUTHORIZED, "유효하지 않거나 만료된 리프레시 토큰입니다.");
        }

        if (!"REFRESH".equals(jwtTokenProvider.getTokenType(refreshToken))) {
            throw new ResponseStatusException(UNAUTHORIZED, "리프레시 토큰만 로그아웃에 사용할 수 있습니다.");
        }

        Token savedToken = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "저장된 리프레시 토큰이 아닙니다."));

        tokenRepository.delete(savedToken);
    }
}
