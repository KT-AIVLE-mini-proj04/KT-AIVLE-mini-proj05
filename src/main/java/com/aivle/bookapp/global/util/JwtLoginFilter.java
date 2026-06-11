package com.aivle.bookapp.global.util;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.dto.auth.LoginResponseDto;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.service.AuthService;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

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

        try {
            LoginRequestDto loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
            Users user = authService.authenticate(loginRequest);

            String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId());

            Token savedToken = tokenRepository.findFirstByUser_UsersIdOrderByTokenIdDesc(user.getUsersId())
                    .orElseGet(Token::new);
            savedToken.setUser(user);
            savedToken.setToken(refreshToken);
            tokenRepository.save(savedToken);

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
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"message\":\"로그인을 실패했습니다.\"}");
        }
    }
}
