package com.sparta.todoapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(NoExistTodoException.class)
    public ResponseEntity<String> noExistTodoHandler() {
        return ResponseEntity.status(400).body("존재하지 않는 일정입니다.");
    }

    @ExceptionHandler(NoAuthorityException.class)
    public ResponseEntity<String> noAuthrotyHandler() {
        return ResponseEntity.status(400).body("권한이 없습니다.");
    }
}
