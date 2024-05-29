package com.sparta.todoapp.entity;

import com.sparta.todoapp.exception.LargeFileSizeException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Getter
@NoArgsConstructor
public class File extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String extension;
    private int size;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] contents;

    public File(MultipartFile file, String extension) throws IOException {
        this.name = file.getOriginalFilename();
        this.extension = extension;
        this.size = (int) file.getSize();
        this.contents = file.getBytes();
    }
}
