package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.TodoRequestDto;
import com.sparta.todoapp.dto.TodoResponseDto;
import com.sparta.todoapp.entity.Todo;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(User user, TodoRequestDto requestDto) {
        Todo todo = new Todo(user, requestDto);
        todoRepository.save(todo);
        TodoResponseDto responseDto = new TodoResponseDto(todo);
        return responseDto;
    }

    public TodoResponseDto choiceInquiryTodo(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 일정은 없습니다.")
        );

        TodoResponseDto responseDto = new TodoResponseDto(todo);
        return responseDto;
    }
}
