package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.TodoRequestDto;
import com.sparta.todoapp.dto.TodoResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping()
    public ResponseEntity<TodoResponseDto> createTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoRequestDto requestDto) {
        return ResponseEntity.status(200).body(todoService.createTodo(userDetails.getUser(), requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> choiceInquiryTodo(@PathVariable Long id) {
        return ResponseEntity.status(200).body(todoService.choiceInquiryTodo(id));
    }

    @GetMapping()
    public ResponseEntity<List<TodoResponseDto>> allInquiryTodo() {
        return ResponseEntity.status(200).body(todoService.allInquiryTodo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> editTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoRequestDto requestDto, @PathVariable Long id) {
        return ResponseEntity.status(200).body(todoService.editTodo(userDetails.getUser(), requestDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return ResponseEntity.status(200).body(todoService.deleteTodo(userDetails.getUser(), id));
    }
}
