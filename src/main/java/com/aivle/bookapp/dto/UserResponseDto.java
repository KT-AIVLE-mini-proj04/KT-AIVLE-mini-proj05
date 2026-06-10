package com.aivle.bookapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String email;
    private String name;
    private String phone;
    private String address;
}
