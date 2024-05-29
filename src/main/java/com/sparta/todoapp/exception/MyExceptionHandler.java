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

    @ExceptionHandler(LargeFileSizeException.class)
    public ResponseEntity<String> largeFileSizeHandler() {
        return ResponseEntity.status(400).body("파일 사이즈가 너무 큽니다. 5MB까지만 가능합니다.");
    }

    @ExceptionHandler(InvalidExtensionException.class)
    public ResponseEntity<String> invalidExtensionHandler() {
        return ResponseEntity.status(400).body("지원하지 않는 확장자입니다. jpg, png 파일만 업로드 가능합니다.");
    }
}
