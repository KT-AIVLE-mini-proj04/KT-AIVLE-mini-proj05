package com.aivle.bookapp.global.util;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    private final SecurityErrorResponseWriter securityErrorResponseWriter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!"/auth/refresh".equals(request.getServletPath())
                || !HttpMethod.POST.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = RefreshTokenCookieSupport.resolveRefreshToken(request);
        if (refreshToken == null || refreshToken.isBlank()) {
            securityErrorResponseWriter.write(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    "리프레시 토큰 쿠키가 없습니다. withCredentials: true 설정을 확인해주세요."
            );
            return;
        }

        if (!jwtTokenProvider.validateToken(refreshToken)
                || !"REFRESH".equals(jwtTokenProvider.getTokenType(refreshToken))) {
            securityErrorResponseWriter.write(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
            return;
        }

        Token savedToken = tokenRepository.findByToken(refreshToken)
                .orElse(null);
        if (savedToken == null) {
            securityErrorResponseWriter.write(response, HttpStatus.UNAUTHORIZED, "저장된 리프레시 토큰이 아닙니다.");
            return;
        }

        String loginId = jwtTokenProvider.getEmailFromToken(refreshToken);
        String accessToken = jwtTokenProvider.createAccessToken(
                loginId,
                jwtTokenProvider.createTokenBindingValue(refreshToken)
        );

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Map.of("accessToken", accessToken));
    }
}
