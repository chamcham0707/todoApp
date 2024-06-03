package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.TodoRequestDto;
import com.sparta.todoapp.dto.TodoResponseDto;
import com.sparta.todoapp.entity.File;
import com.sparta.todoapp.entity.Todo;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.exception.InvalidExtensionException;
import com.sparta.todoapp.exception.LargeFileSizeException;
import com.sparta.todoapp.exception.NoAuthorityException;
import com.sparta.todoapp.exception.NoExistTodoException;
import com.sparta.todoapp.repository.FileRepository;
import com.sparta.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final FileRepository fileRepository;

    public TodoResponseDto createTodo(User user, TodoRequestDto requestDto, MultipartFile file) throws IOException {
        File newFile = null;
        if (file != null) {
            validateFile(file);

            newFile = new File(file, getFileExtension(file.getOriginalFilename()));
            fileRepository.save(newFile);
        }

        Todo todo = new Todo(user, requestDto, newFile);
        todoRepository.save(todo);

        TodoResponseDto responseDto = new TodoResponseDto(todo);
        return responseDto;
    }


    public TodoResponseDto choiceInquiryTodo(Long id) {
        Todo todo = findTodo(id);

        TodoResponseDto responseDto = new TodoResponseDto(todo);
        return responseDto;
    }

    public List<TodoResponseDto> allInquiryTodo() {
        List<Todo> todoList = todoRepository.findAllByOrderByModifiedAtDesc().orElseThrow(
                () -> new IllegalArgumentException("등록되어 있는 일정이 없습니다.")
        );

        return todoList.stream().map(TodoResponseDto::new).toList();
    }

    public TodoResponseDto editTodo(User user, TodoRequestDto requestDto, Long id) {
        Todo todo = findTodo(id);

        if (!Objects.equals(todo.getUser().getId(), user.getId())) {
            throw new NoAuthorityException();
        }

        todo.update(requestDto);
        todoRepository.save(todo);

        TodoResponseDto responseDto = new TodoResponseDto(todo);

        return responseDto;
    }

    public String deleteTodo(User user, Long id) throws NoExistTodoException {
        Todo todo = findTodo(id);

        if (!Objects.equals(todo.getUser().getId(), user.getId())) {
            throw new NoAuthorityException();
        }

        todoRepository.delete(todo);

        return "삭제가 완료되었습니다.";
    }

    private Todo findTodo(Long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new NoExistTodoException()
        );
    }

    private void validateFile(MultipartFile file) {
        if ((file.getSize() / 1024 / 1024) > 5) {
            throw new LargeFileSizeException();
        }

        if (!Objects.equals(getFileExtension(file.getOriginalFilename()), "jpg") && !Objects.equals(getFileExtension(file.getOriginalFilename()), "png")) {
            throw new InvalidExtensionException();
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}