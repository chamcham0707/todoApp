package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Todo;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.exception.NoAuthorityException;
import com.sparta.todoapp.exception.NoContentException;
import com.sparta.todoapp.exception.NoExistCommentException;
import com.sparta.todoapp.exception.NoExistTodoException;
import com.sparta.todoapp.repository.CommentRepository;
import com.sparta.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;


    public CommentResponseDto createComment(User user, Long todoId, CommentRequestDto requestDto) {
        checkContentEmpty(requestDto);

        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new NoExistTodoException()
        );

        Comment comment = new Comment(requestDto, user, todo);
        commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto(comment);
        return responseDto;
    }

    public CommentResponseDto editComment(User user, Long commentId, CommentRequestDto requestDto) {
        checkContentEmpty(requestDto);

        // 1. 수정하고자 하는 사용자가 댓글을 작성한 사용자인지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NoExistCommentException()
        );

        if (comment.getUser().getId() != user.getId()) {
            throw new NoAuthorityException();
        }

        // 2. 댓글 수정 후 DB 저장
        comment.update(requestDto);
        commentRepository.save(comment);

        // 3. responseDto 반환
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        return responseDto;
    }

    private void checkContentEmpty(CommentRequestDto requestDto) {
        if (requestDto.getContent() == "") {
            throw new NoContentException();
        }
    }
}
