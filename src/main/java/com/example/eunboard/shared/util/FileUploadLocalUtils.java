package com.example.eunboard.shared.util;

import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadLocalUtils implements FileUpload{
    @Value("${spring.default-image-local}")
    private String defaultImageUrl;
    @Value("${spring.root-dir}")
    private String rootDir;

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public String upload(MultipartFile multipartFile, String dirName) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = createStoreFileName(originalFilename);
        log.info("i am local {}", fileName);
        Path uploadPath = Paths.get(System.getProperty("user.dir") + "/" + dirName);
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
        return fileName;
    }

    public void delete(Long userId, String dir) {
        // 기본 이미지가 아니라면 삭제 진행
        if (!dir.equals(defaultImageUrl)) {
            Path dirPath = Paths.get(System.getProperty("user.dir") + rootDir + "/" + userId);

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

    private void validateFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_EMPTY.getMessage(), ErrorCode.FILE_EMPTY);
        }
    }

    private String extractExt(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1); // 확장자
    }

    private String createStoreFileName(String filename) {
        String ext = extractExt(filename);
        MessageDigest mdMD5;
        try {
            mdMD5 = MessageDigest.getInstance("MD5");
            mdMD5.update(filename.getBytes("UTF-8"));
            byte[] md5Hash = mdMD5.digest();
            StringBuilder hexMD5hash = new StringBuilder();
            for (byte b : md5Hash) {
                String hexString = String.format("%02x", b);
                hexMD5hash.append(hexString);
            }
            return hexMD5hash.toString()+"."+ext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "default.png";
    }
}
