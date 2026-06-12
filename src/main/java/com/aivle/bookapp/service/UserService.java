package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.UserResponseDto;
import com.aivle.bookapp.dto.users.SignUpRequestDto;
import com.aivle.bookapp.global.util.BcryptPassword;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.CONFLICT;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
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
        // DTO 검증은 컨트롤러의 @Valid에서 처리하고, 서비스는 비즈니스 검증만 담당한다.
        if (userRepository.existsByLoginId(user.getLoginId())){
            throw new ResponseStatusException(CONFLICT, "이미 사용 중인 아이디입니다.");
        }
    }

    public UserResponseDto signup(SignUpRequestDto user) {
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
