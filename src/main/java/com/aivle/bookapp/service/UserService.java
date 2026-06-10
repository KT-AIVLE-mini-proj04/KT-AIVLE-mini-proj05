package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.UserResponseDto;
import com.aivle.bookapp.dto.users.SignUpRequestDto;
import com.aivle.bookapp.dto.users.SignUpResponseDto;
import com.aivle.bookapp.global.util.Sha256Util;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto signup(@RequestBody SignUpRequestDto user) {
        // Request Body 내용으로 User 인스턴스 생성
        User newUser = new User();

        String passwordRaw = user.getPassword();
        String passwordHash = Sha256Util.encrypt(passwordRaw);
        newUser.setPassword(passwordHash);
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPhoneNumber(user.getPhone());
        newUser.setAddress(user.getAddress());

        // User 테이블에 저장
        userRepository.save(newUser);

        // 회원가입 Response 객체 생성
        SignUpResponseDto userResponseDto = new SignUpResponseDto();
        userResponseDto.setStatus("success");
        userResponseDto.setReason("");
        userResponseDto.setEmail(newUser.getEmail());
        userResponseDto.setName(newUser.getName());
        userResponseDto.setPhone(newUser.getPhoneNumber());
        userResponseDto.setAddress(newUser.getAddress());

        // 반환
        return userResponseDto;
    }
}
