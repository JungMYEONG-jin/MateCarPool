package com.example.eunboard.shared.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadUtils {
    // s3 쓸지 로컬 쓸지 결정
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${spring.bucket}")
    private String bucket;
    @Value("${spring.full-bucket}")
    private String prefix;
    private static String fileDir;
    // s3 uploader
    private final AmazonS3Client amazonS3Client;

    @PostConstruct
    private void init() {
        this.fileDir = System.getProperty("user.dir");
    }

    public String upload(MultipartFile multipartFile, String dirName) {
        File file = null;
        try {
            file = convert(dirName, multipartFile).orElseThrow(() -> new IllegalArgumentException("Convert MultiPartFile to File failed..."));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return upload(file, dirName);
    }

    public String delete(String dirName) {
        amazonS3Client.deleteObject(bucket, dirName.substring(prefix.length()));
        return dirName + " Delete Success";
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadURL = putS3(uploadFile, fileName);
        log.info("dirName {}", fileDir + "/" + dirName);
        return uploadURL;
    }

    private String putS3(File file, String filename) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, filename, file).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, filename).toString();
    }

    // local save
    private Optional<File> convert(String dirName, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            log.info("empty {}", dirName);
            return Optional.empty();
        }
        // get real path
        String realPath = fileDir + "/" + dirName;
        // create directory
        Path uploadPath = Paths.get(realPath);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                log.error("Could createDirectories: " + dirName, e);
            }
        } else {
            // 업데이트시 기존 폴더 제거
            cleanAbsolutePath(realPath);
            // 폴더 생성
            Files.createDirectories(uploadPath);
        }
        // create file
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        File file = new File(realPath + "/" + storeFileName);
        multipartFile.transferTo(file);
        return Optional.of(file);
    }

    public static void cleanAbsolutePath(String dir) {
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
