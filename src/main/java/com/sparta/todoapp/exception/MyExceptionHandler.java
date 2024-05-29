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
        return ResponseEntity.status(400).body("작성자만 삭제/수정할 수 있습니다.");
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String> noContentHandler() {
        return ResponseEntity.status(400).body("내용이 없습니다.");
    }

    @ExceptionHandler(NoExistCommentException.class)
    public ResponseEntity<String> noExistCommentHandler() {
        return ResponseEntity.status(400).body("존재하지 않는 댓글입니다.");
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> invalidTokenHandler() {
        return ResponseEntity.status(400).body("토큰이 유효하지 않습니다.");
    }
}
