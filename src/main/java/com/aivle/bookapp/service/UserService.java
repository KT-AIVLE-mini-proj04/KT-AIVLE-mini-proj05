package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.UserResponseDto;
import com.aivle.bookapp.dto.users.SignUpRequestDto;
import com.aivle.bookapp.dto.users.SignUpResponseDto;
import com.aivle.bookapp.global.util.BcryptPassword;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto signup(@RequestBody SignUpRequestDto user) {
        System.out.println(user.toString());

        // Request Body 내용으로 User 인스턴스 생성
        User newUser = new User();

        String passwordRaw = user.getPassword();
        String passwordHash = BcryptPassword.encrypt(passwordRaw);
        newUser.setLoginId(user.getLoginId());
        newUser.setPassword(passwordHash);
        newUser.setName(user.getName());
        newUser.setGubun(user.getGubun());
        newUser.setEmail(user.getEmail());
        newUser.setAddress(user.getAddress());
        newUser.setPhoneNumber(user.getPhone());

        // User 테이블에 저장
        userRepository.save(newUser);

        // 회원가입 Response 객체 생성
        SignUpResponseDto userResponseDto = new SignUpResponseDto();
        userResponseDto.setLoginId(user.getLoginId());
        userResponseDto.setName(user.getName());
        userResponseDto.setGubun(user.getGubun());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setAddress(user.getAddress());
        userResponseDto.setPhoneNumber(user.getPhone());
        userResponseDto.setStatus("success");
        userResponseDto.setReason("");

        // 반환
        return userResponseDto;
    }
}
