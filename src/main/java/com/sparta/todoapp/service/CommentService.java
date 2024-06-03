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

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;


    public CommentResponseDto createComment(User user, Long todoId, CommentRequestDto requestDto) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new NoExistTodoException()
        );

        Comment comment = new Comment(requestDto, user, todo);

        CommentResponseDto responseDto = new CommentResponseDto(commentRepository.save(comment));
        return responseDto;
    }

    public CommentResponseDto editComment(User user, Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NoExistCommentException()
        );

        if (!Objects.equals(comment.getUser().getId() , user.getId())) {
            throw new NoAuthorityException();
        }

        comment.update(requestDto);

        CommentResponseDto responseDto = new CommentResponseDto(commentRepository.save(comment));
        return responseDto;
    }

    public String deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NoExistCommentException()
        );

        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new NoAuthorityException();
        }

        commentRepository.delete(comment);
        return "성공적으로 삭제되었습니다.";
    }
}
