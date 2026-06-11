package com.aivle.bookapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Integer usersId;

    @Column(name = "login_id", length = 255)
    @NotNull
    private String loginId;

    @Column(name = "password", nullable = false, length = 255)
    @NotNull
    private String password;

    @Column(name = "name", length = 20)
    @NotNull
    private String name;

    @Column(name = "gubun")
    private Integer gubun;

    @Column(name = "email", length = 255)
    @NotNull
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "이메일 형식이 올바르지 않습니다."
    )
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone_number", length = 11)
    @Pattern(regexp = "^$|^\\d{11}$", message = "휴대폰 번호는 '-' 없이 숫자 11자리여야 합니다.")
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
