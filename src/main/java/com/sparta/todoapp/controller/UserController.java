package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "UserController")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        System.out.println("input signup controller");
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errorMessages.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }

        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }
}
