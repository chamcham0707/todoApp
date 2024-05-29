package com.sparta.todoapp.jwt;

public enum TokenErrorEnum {
    INVALID_SIGNATURE("유효하지 않은 서명"),
    EXPIRE("만료"),
    UNSSPORT("지원하지 않음"),
    EMPTY("유효하지 않음"),
    NOT_ERROR("에러 없음");

    final private String error;
    TokenErrorEnum(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }
}
