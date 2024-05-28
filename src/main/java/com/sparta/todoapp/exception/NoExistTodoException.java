package com.sparta.todoapp.exception;

public class NoExistTodoException extends RuntimeException {
    public NoExistTodoException() {
    }

    public NoExistTodoException(String message) {
        super("존재하지 않는 일정입니다.");
    }
}
