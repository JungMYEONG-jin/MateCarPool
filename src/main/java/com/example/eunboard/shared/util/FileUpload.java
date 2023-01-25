package com.example.eunboard.shared.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpload {
    void delete(Long userId, String dirName);
    String upload(MultipartFile multipartFile, String dirName);
    String getDefaultImageUrl();
}
