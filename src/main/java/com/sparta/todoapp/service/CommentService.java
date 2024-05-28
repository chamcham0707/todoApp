package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Todo;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.exception.NoContentException;
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
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new NoExistTodoException()
        );

        if (requestDto.getContent() == "") {
            throw new NoContentException();
        }

        Comment comment = new Comment(requestDto, user, todo);
        commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto(comment);
        return responseDto;
    }
}
