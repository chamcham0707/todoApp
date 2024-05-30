package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.exception.LoginFailException;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<String> signup(SignupRequestDto requestDto) {
        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent()) {
            return ResponseEntity.status(400).body("중복된 username 입니다.: " + requestDto.getUsername());
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                return ResponseEntity.status(400).body("관리자 암호가 틀렸습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(requestDto, role);
        userRepository.save(user);

        return ResponseEntity.status(200).body("회원가입이 성공하였습니다.");
    }

    public ResponseEntity<String> login(LoginRequestDto requestDto, HttpServletResponse response) {

        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 USER 입니다.")
        );

        if (Objects.equals(user.getPassword(), requestDto.getPassword())) {
            // 로그인 성공 - 토큰 발급해주기
            String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole());

            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
            response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

            return ResponseEntity.status(200).body("로그인에 성공하였습니다.");
        } else {
            // 로그인 실패
            throw new LoginFailException();
        }
    }
}
