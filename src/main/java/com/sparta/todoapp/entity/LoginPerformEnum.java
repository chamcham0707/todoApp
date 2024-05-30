package com.sparta.todoapp.entity;

public enum LoginPerformEnum {
    BEFORE(PerformType.BEFORE),
    SUCCESS(PerformType.SUCCESS),
    FAILURE(PerformType.FAILURE);

    private final String performType;

    LoginPerformEnum(String performType) {
        this.performType = performType;
    }

    public String getPerformType() {
        return this.performType;
    }

    public static class PerformType {
        public static final String BEFORE = "LOGIN_BEFORE";
        public static final String SUCCESS = "LOGIN_SUCCESS";
        public static final String FAILURE = "LOGIN_FAILURE";
    }
}
