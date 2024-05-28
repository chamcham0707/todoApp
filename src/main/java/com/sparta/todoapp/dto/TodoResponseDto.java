package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime modifiedAt;

    public TodoResponseDto(Todo todo) {
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.modifiedAt = todo.getModifiedAt();
        this.nickname = todo.getUser().getNickname();
    }
}
