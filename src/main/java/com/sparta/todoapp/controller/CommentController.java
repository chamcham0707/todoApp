package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{todoId}")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long todoId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.status(200).body(commentService.createComment(userDetails.getUser(), todoId, requestDto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> editComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.status(200).body(commentService.editComment(userDetails.getUser(), commentId, requestDto));
    }
}
