package com.aivle.bookapp.global.util;

import com.aivle.bookapp.domain.Token;
import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtLoginFilterTest {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private TokenRepository tokenRepository;
    private ObjectMapper objectMapper;
    private JwtLoginFilter jwtLoginFilter;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        tokenRepository = mock(TokenRepository.class);
        objectMapper = new ObjectMapper();

        SecurityErrorResponseWriter securityErrorResponseWriter = new SecurityErrorResponseWriter(objectMapper);
        JwtAuthenticationFailureHandler failureHandler =
                new JwtAuthenticationFailureHandler(securityErrorResponseWriter);

        jwtLoginFilter = new JwtLoginFilter(
                authenticationManager,
                failureHandler,
                jwtTokenProvider,
                tokenRepository,
                objectMapper,
                securityErrorResponseWriter
        );
    }

    @Test
    void malformedLoginRequestReturnsBadRequestErrorResponse() throws Exception {
        MockHttpServletResponse response = doLoginRequest("{");

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8))
                .contains("\"status\":400")
                .contains("\"message\":\"로그인을 실패했습니다.\"")
                .contains("\"details\":null");
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void blankLoginRequestReturnsBadRequestErrorResponse() throws Exception {
        MockHttpServletResponse response = doLoginRequest("""
                {
                  "loginId": "",
                  "password": "password123"
                }
                """);

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8))
                .contains("\"status\":400")
                .contains("\"message\":\"로그인을 실패했습니다.\"")
                .contains("\"details\":null");
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void authenticationFailureReturnsBadRequestErrorResponse() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad credentials"));

        MockHttpServletResponse response = doLoginRequest("""
                {
                  "loginId": "tester",
                  "password": "wrong-password"
                }
                """);

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8))
                .contains("\"status\":400")
                .contains("\"message\":\"로그인을 실패했습니다.\"")
                .contains("\"details\":null");
    }

    @Test
    void successfulLoginReturnsExistingLoginResponse() throws Exception {
        Users user = new Users();
        user.setUsersId(1);
        user.setLoginId("tester");
        user.setName("Tester");
        user.setGubun(1);
        user.setEmail("tester@example.com");
        user.setAddress("Seoul");
        user.setPhoneNumber("01012345678");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                java.util.Collections.emptyList()
        );

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.createRefreshToken("tester")).thenReturn("refresh-token");
        when(jwtTokenProvider.createTokenBindingValue("refresh-token")).thenReturn("binding-value");
        when(jwtTokenProvider.createAccessToken("tester", "binding-value")).thenReturn("access-token");
        when(jwtTokenProvider.getRefreshTokenExpirationMs()).thenReturn(3600000L);
        when(tokenRepository.findFirstByUser_UsersIdOrderByTokenIdDesc(1)).thenReturn(Optional.empty());
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MockHttpServletResponse response = doLoginRequest("""
                {
                  "loginId": "tester",
                  "password": "password123"
                }
                """);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8))
                .contains("\"accessToken\":\"access-token\"")
                .contains("\"loginId\":\"tester\"")
                .contains("\"email\":\"tester@example.com\"");

        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(tokenCaptor.getValue().getToken()).isEqualTo("refresh-token");
    }

    private MockHttpServletResponse doLoginRequest(String body) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");
        request.setServletPath("/auth/login");
        request.setContentType("application/json");
        request.setContent(body.getBytes(StandardCharsets.UTF_8));

        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtLoginFilter.doFilter(request, response, new MockFilterChain());
        return response;
    }
}
