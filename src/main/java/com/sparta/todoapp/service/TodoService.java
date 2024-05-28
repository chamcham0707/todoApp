package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.TodoRequestDto;
import com.sparta.todoapp.dto.TodoResponseDto;
import com.sparta.todoapp.entity.Todo;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<TodoResponseDto> allInquiryTodo() {
        List<Todo> todoList = todoRepository.findAllByOrderByModifiedAtDesc().orElseThrow(
                () -> new IllegalArgumentException("등록되어 있는 일정이 없습니다.")
        );

        return todoList.stream().map(TodoResponseDto::new).toList();
    }

    public TodoResponseDto editTodo(User user, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("수정할 권한이 없습니다.")
        );

        todo.update(requestDto);
        todoRepository.save(todo);

        TodoResponseDto responseDto = new TodoResponseDto(todo);

        return responseDto;
    }
}
