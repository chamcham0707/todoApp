package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.entity.LoginLog;
import com.sparta.todoapp.entity.LoginPerformEnum;
import com.sparta.todoapp.repository.LoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;

    public void saveLoginLog(LoginRequestDto requestDto, LoginPerformEnum loginPerformEnum) {
        LoginLog log = new LoginLog(requestDto, loginPerformEnum);
        loginLogRepository.save(log);
    }
}
