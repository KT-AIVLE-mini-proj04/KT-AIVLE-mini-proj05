package com.aivle.bookapp.dto.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    private Integer gubun;

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "이메일 형식이 올바르지 않습니다."
    )
    private String email;

    private String address;

    @JsonProperty("phoneNumber")
    @JsonAlias("phone")
    @Pattern(regexp = "^$|^\\d{11}$", message = "휴대폰 번호는 '-' 없이 숫자 11자리여야 합니다.")
    private String phoneNumber;
}
