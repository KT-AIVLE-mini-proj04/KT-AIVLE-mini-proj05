package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.UserResponseDto;
import com.aivle.bookapp.dto.users.SignUpRequestDto;
import com.aivle.bookapp.global.util.BcryptPassword;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_NUMBER_REGEX = "^\\d{11}$";

    private final UserRepository userRepository;

    private Users setUser(SignUpRequestDto user) {
        Users newUser = new Users();

        String passwordRaw = user.getPassword();
        String passwordHash = BcryptPassword.encrypt(passwordRaw);
        newUser.setLoginId(user.getLoginId());
        newUser.setPassword(passwordHash);
        newUser.setName(user.getName());
        newUser.setGubun(user.getGubun());
        newUser.setEmail(user.getEmail());
        newUser.setAddress(user.getAddress());
        newUser.setPhoneNumber(user.getPhoneNumber());
        return newUser;
    }

    private UserResponseDto setUserResponseDto(Users user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUsersId(user.getUsersId());
        userResponseDto.setLoginId(user.getLoginId());
        userResponseDto.setName(user.getName());
        userResponseDto.setGubun(user.getGubun());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setAddress(user.getAddress());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }

    private void invalidUserInfo(SignUpRequestDto user) {
        // 1. 회원가입 요청폼 빈 값 확인
        if (user.getLoginId() == null || user.getLoginId().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank() ||
                user.getName() == null || user.getName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "필수 회원 정보가 비어 있습니다.");
        }

        // 2. 기존 User들의 ID 비교
        if (userRepository.existsByLoginId(user.getLoginId())){
            throw new ResponseStatusException(CONFLICT, "이미 사용 중인 아이디입니다.");
        }

        // 3. 이메일 형식 검증
        if (!user.getEmail().matches(EMAIL_REGEX)) {
            throw new ResponseStatusException(BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
        }

        // 4. 전화번호 형식 검증: 값이 있을 때만 검사
        if (user.getPhoneNumber() != null &&
                !user.getPhoneNumber().isBlank() &&
                !user.getPhoneNumber().matches(PHONE_NUMBER_REGEX)) {
            throw new ResponseStatusException(BAD_REQUEST, "휴대폰 번호는 '-' 없이 숫자 11자리여야 합니다.");
        }
    }

    public UserResponseDto signup(@RequestBody SignUpRequestDto user) {
        // 회원가입 폼 유효성 검사
        invalidUserInfo(user);

        // Request Body 내용으로 User 인스턴스 생성
        Users newUser = setUser(user);

        // User 테이블에 저장
        Users savedUser = userRepository.save(newUser);

        // 회원가입 Response 객체 생성
        UserResponseDto userResponseDto = setUserResponseDto(savedUser);

        // 반환
        return userResponseDto;
    }
}
