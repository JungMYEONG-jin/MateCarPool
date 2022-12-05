package com.example.eunboard.shared.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileUploadUtilsTest {

    @Test
    void name() {
        String uploadDir = "/image/profiles/3";
        Path uploadPath = Paths.get(System.getProperty("user.dir") + uploadDir);
        System.out.println("uploadPath = " + uploadPath.toString());
    }
}