package com.aivle.bookapp.global.config;

import com.aivle.bookapp.global.util.JwtAuthenticationFailureHandler;
import com.aivle.bookapp.global.util.JwtAccessDeniedHandler;
import com.aivle.bookapp.global.util.JwtAuthenticationEntryPoint;
import com.aivle.bookapp.global.util.JwtAuthenticationFilter;
import com.aivle.bookapp.global.util.JwtLoginFilter;
import com.aivle.bookapp.global.util.JwtRefreshFilter;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.global.util.SecurityErrorResponseWriter;
import com.aivle.bookapp.repository.TokenRepository;
import com.aivle.bookapp.service.CustomUserDetailsService;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtLoginFilter jwtLoginFilter,
            JwtRefreshFilter jwtRefreshFilter
    ) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/login", "/auth/refresh", "/auth/logout", "/users", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRefreshFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter(
            AuthenticationManager authenticationManager,
            JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler,
            JwtTokenProvider jwtTokenProvider,
            TokenRepository tokenRepository,
            ObjectMapper objectMapper
    ) {
        return new JwtLoginFilter(
                authenticationManager,
                jwtAuthenticationFailureHandler,
                jwtTokenProvider,
                tokenRepository,
                objectMapper
        );
    }

    @Bean
    public JwtRefreshFilter jwtRefreshFilter(
            JwtTokenProvider jwtTokenProvider,
            TokenRepository tokenRepository,
            ObjectMapper objectMapper,
            SecurityErrorResponseWriter securityErrorResponseWriter
    ) {
        return new JwtRefreshFilter(
                jwtTokenProvider,
                tokenRepository,
                objectMapper,
                securityErrorResponseWriter
        );
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
