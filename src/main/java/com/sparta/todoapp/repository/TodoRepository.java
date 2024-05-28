package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByUserId(Long id);
}
