package com.example.eunboard.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploadUtils {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(System.getProperty("user.dir") + uploadDir);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                log.error("Could createDirectories: " + fileName, e);
            }
        }

        try (InputStream iStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(iStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error("Could not save file: " + fileName, e);
        }
    }

    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        log.error("Could not delete file: " + file, ex);
                    }
                }
            });
        } catch (IOException e) {
            log.error("Could not list directory: ", e);
        }
    }
}
