package com.aivle.bookapp.global.util;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.dto.auth.LoginResponseDto;
import com.aivle.bookapp.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@RequiredArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {
    private static final String LOGIN_FAILURE_MESSAGE = "로그인을 실패했습니다.";

    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
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

        if (!"/auth/login".equals(request.getServletPath())
                || !HttpMethod.POST.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        LoginRequestDto loginRequest;
        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            securityErrorResponseWriter.write(response, HttpStatus.BAD_REQUEST, LOGIN_FAILURE_MESSAGE);
            return;
        }

        if (isBlank(loginRequest.getLoginId()) || isBlank(loginRequest.getPassword())) {
            securityErrorResponseWriter.write(response, HttpStatus.BAD_REQUEST, LOGIN_FAILURE_MESSAGE);
            return;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword())
            );
            Users user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

            String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId());

            Token savedToken = tokenRepository.findFirstByUser_UsersIdOrderByTokenIdDesc(user.getUsersId())
                    .orElseGet(Token::new);
            savedToken.setUser(user);
            savedToken.setToken(refreshToken);
            tokenRepository.save(savedToken);

            String accessToken = jwtTokenProvider.createAccessToken(
                    user.getLoginId(),
                    jwtTokenProvider.createTokenBindingValue(refreshToken)
            );

            ResponseCookie refreshTokenCookie = ResponseCookie.from(
                            RefreshTokenCookieSupport.REFRESH_TOKEN_COOKIE_NAME,
                            refreshToken
                    )
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(Duration.ofMillis(jwtTokenProvider.getRefreshTokenExpirationMs()))
                    .build();
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

            LoginResponseDto loginResponse = new LoginResponseDto();
            loginResponse.setAccessToken(accessToken);
            loginResponse.setUsersId(user.getUsersId());
            loginResponse.setLoginId(user.getLoginId());
            loginResponse.setName(user.getName());
            loginResponse.setGubun(user.getGubun());
            loginResponse.setEmail(user.getEmail());
            loginResponse.setAddress(user.getAddress());
            loginResponse.setPhoneNumber(user.getPhoneNumber());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), loginResponse);
        } catch (AuthenticationException e) {
            jwtAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
