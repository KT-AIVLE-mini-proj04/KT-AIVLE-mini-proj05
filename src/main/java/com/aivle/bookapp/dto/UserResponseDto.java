package com.aivle.bookapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long usersId;
    private String loginId;
    private String name;
    private Integer gubun;
    private String email;
    private String address;
    private String phoneNumber;
}
