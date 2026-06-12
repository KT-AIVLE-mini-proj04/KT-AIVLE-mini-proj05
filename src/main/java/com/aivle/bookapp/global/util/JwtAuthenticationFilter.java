package com.aivle.bookapp.global.util;

import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/",
            "/auth/login",
            "/users",
            "/error"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token == null) {
            log.debug("No Authorization Bearer token for path={}", request.getServletPath());
        } else if (!jwtTokenProvider.validateToken(token)) {
            log.warn("Invalid JWT token for path={}", request.getServletPath());
        } else {
            String tokenType = jwtTokenProvider.getTokenType(token);

            if ("ACCESS".equals(tokenType)) {
                String loginId = jwtTokenProvider.getEmailFromToken(token);
                String sessionKey = jwtTokenProvider.getSessionKey(token);
                Token savedToken = tokenRepository.findFirstByUser_LoginIdOrderByTokenIdDesc(loginId)
                        .orElse(null);

                if (savedToken == null) {
                    log.warn("No saved refresh token found for loginId={} path={}", loginId, request.getServletPath());
                    filterChain.doFilter(request, response);
                    return;
                }

                String currentSessionKey = jwtTokenProvider.createTokenBindingValue(savedToken.getToken());
                if (!currentSessionKey.equals(sessionKey)) {
                    log.warn("Access token session mismatch for loginId={} path={}", loginId, request.getServletPath());
                    filterChain.doFilter(request, response);
                    return;
                }

                Users user = userRepository.findByLoginId(loginId)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.emptyList()
                        );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            } else {
                log.warn("Unsupported tokenType={} for path={}", tokenType, request.getServletPath());
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return EXCLUDED_PATHS.contains(path);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return bearerToken.substring(BEARER_PREFIX.length());
    }
}
