package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
