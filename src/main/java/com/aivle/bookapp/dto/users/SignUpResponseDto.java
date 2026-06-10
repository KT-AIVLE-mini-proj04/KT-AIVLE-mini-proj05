package com.aivle.bookapp.dto.users;

import com.aivle.bookapp.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto extends UserResponseDto {
    private String status;
    private String reason;
}
