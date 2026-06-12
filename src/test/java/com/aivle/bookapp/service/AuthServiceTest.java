package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void logoutFailsWhenRefreshTokenCookieIsMissing() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.logout(null)
        );

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("리프레시 토큰 쿠키가 없습니다.", exception.getReason());
        verify(tokenRepository, never()).findByToken(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void logoutFailsWhenRefreshTokenIsInvalid() {
        String refreshToken = "invalid-refresh-token";
        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.logout(refreshToken)
        );

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("유효하지 않거나 만료된 리프레시 토큰입니다.", exception.getReason());
        verify(tokenRepository, never()).findByToken(refreshToken);
    }

    @Test
    void logoutFailsWhenTokenIsNotStored() {
        String refreshToken = "stored-refresh-token";
        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.getTokenType(refreshToken)).thenReturn("REFRESH");
        when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.logout(refreshToken)
        );

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("저장된 리프레시 토큰이 아닙니다.", exception.getReason());
    }

    @Test
    void logoutDeletesStoredRefreshToken() {
        String refreshToken = "stored-refresh-token";
        Token savedToken = new Token();

        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.getTokenType(refreshToken)).thenReturn("REFRESH");
        when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(savedToken));

        authService.logout(refreshToken);

        verify(tokenRepository).delete(savedToken);
    }
}
