package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.LoginRequestDto;
import jakarta.persistence.*;

@Entity
public class LoginLog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @Enumerated(value = EnumType.STRING)
    private LoginPerformEnum performType;

    public LoginLog(LoginRequestDto requestDto, LoginPerformEnum performType) {
        this.username = requestDto.getUsername();
        this.performType = performType;
    }
}
