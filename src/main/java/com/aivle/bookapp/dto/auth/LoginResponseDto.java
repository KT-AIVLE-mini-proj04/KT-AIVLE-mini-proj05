package com.aivle.bookapp.dto.auth;

import com.aivle.bookapp.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto extends UserResponseDto {
    private String accessToken;
}
