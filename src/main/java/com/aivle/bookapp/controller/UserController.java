package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.users.SignUpRequestDto;
import com.aivle.bookapp.dto.UserResponseDto;
import com.aivle.bookapp.dto.users.SignUpResponseDto;
import com.aivle.bookapp.global.util.Sha256Util;
import com.aivle.bookapp.repository.UserRepository;
import com.aivle.bookapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public UserResponseDto signup(@RequestBody SignUpRequestDto user) {
        return userService.signup(user);
    }
}
