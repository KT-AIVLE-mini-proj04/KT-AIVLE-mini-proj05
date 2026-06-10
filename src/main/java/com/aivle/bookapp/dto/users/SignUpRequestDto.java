package com.aivle.bookapp.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String loginId;
    private String password;
    private String name;
    private Integer gubun;
    private String email;
    private String address;
    private String phone;
}
