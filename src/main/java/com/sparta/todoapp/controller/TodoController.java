package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.TodoRequestDto;
import com.sparta.todoapp.dto.TodoResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping()
    public ResponseEntity<TodoResponseDto> createTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoRequestDto requestDto) {
        return ResponseEntity.status(200).body(todoService.createTodo(userDetails.getUser(), requestDto));
    }
}
