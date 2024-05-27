package com.sparta.todoapp.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String nickname;
    private String username;
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}
