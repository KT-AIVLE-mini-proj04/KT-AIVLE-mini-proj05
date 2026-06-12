package com.aivle.bookapp.controller;

import com.aivle.bookapp.global.util.RefreshTokenCookieSupport;
import com.aivle.bookapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String refreshToken = RefreshTokenCookieSupport.resolveRefreshToken(request);
        authService.logout(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, RefreshTokenCookieSupport.expireRefreshTokenCookie().toString())
                .body(Map.of("message", "로그아웃되었습니다."));
    }
}
