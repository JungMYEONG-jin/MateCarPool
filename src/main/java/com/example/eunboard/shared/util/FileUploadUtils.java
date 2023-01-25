package com.example.eunboard.shared.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    @Value("${spring.default-image}")
    private String defaultImageUrl;
    // s3 uploader
    private final AmazonS3Client amazonS3Client;


    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    /**
     * s3 file upload
     * @param multipartFile
     * @param dirName
     * @return
     */
    public String upload(MultipartFile multipartFile, String dirName) {
        validateFile(multipartFile);
        String storeFileName = dirName + "/" + createStoreFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, storeFileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED.getMessage(), ErrorCode.FILE_UPLOAD_FAILED);
        }
        return amazonS3Client.getUrl(bucket, storeFileName).toString();
    }

    /**
     * s3 file delete
     * @param dirName
     * @return
     */
    public String delete(String dirName) {
        amazonS3Client.deleteObject(bucket, dirName.substring(prefix.length()));
        return dirName + " Delete Success";
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
