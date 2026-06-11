package com.aivle.bookapp.dto.users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shortPhoneNumberShouldFailValidation() {
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setLoginId("tester");
        dto.setPassword("password123");
        dto.setName("tester");
        dto.setGubun(1);
        dto.setEmail("example2@gmail.com");
        dto.setPhoneNumber("0100000");

        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        assertThat(dto.getPhoneNumber()).isEqualTo("0100000");
        assertThat(violations)
            .extracting(violation -> violation.getPropertyPath().toString())
            .contains("phoneNumber");
    }
}
