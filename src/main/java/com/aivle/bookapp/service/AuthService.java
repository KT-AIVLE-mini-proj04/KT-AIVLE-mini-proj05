package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.auth.LoginRequestDto;
import com.aivle.bookapp.global.util.BcryptPassword;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public Users authenticate(LoginRequestDto user) {
        String loginId = user.getLoginId();
        String passwordRaw = user.getPassword();

        Users existedUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("로그인을 실패했습니다."));
        if (!BcryptPassword.matches(passwordRaw, existedUser.getPassword())) {
            throw new RuntimeException("로그인을 실패했습니다.");
        }

        return existedUser;
    }

}
