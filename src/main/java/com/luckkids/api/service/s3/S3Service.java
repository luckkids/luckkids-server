package com.luckkids.api.service.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service{

    @Value("${s3.bucketName}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public String preview(String fileName, HttpServletResponse response){
        try {
            String previewFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucketName, fileName));
            S3ObjectInputStream objectInputStream = o.getObjectContent();
            byte[] fileByte = IOUtils.toByteArray(objectInputStream);

            String contentType =  URLConnection.guessContentTypeFromName(fileName);

            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+previewFileName+"\"");
            response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

            response.getOutputStream().write(fileByte);
            response.getOutputStream().flush();
            response.getOutputStream().close();

            return new String(fileByte);
        } catch (Exception e) {
            throw new LuckKidsException(ErrorCode.S3_PREVIEW);
        }
    }

    public ResponseEntity<byte[]> download(String fileName){
		try {
            S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucketName, fileName));
            S3ObjectInputStream objectInputStream = o.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String downloadFile = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", downloadFile);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
            throw new LuckKidsException(ErrorCode.S3_DOWNLOAD);
		}
	}

    public String saveImage(MultipartFile multipartFile) {
        try {
            String fileName = getFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, fileName, multipartFile.getInputStream(), objectMetadata);

            return amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch(Exception e) {
            throw new LuckKidsException(ErrorCode.S3_DOWNLOAD);
        }
    }

    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    public String getFileName(String originName) {
        return originName + UUID.randomUUID() + "." + extractExtension(originName);
    }

    // 이미지 파일의 확장자를 추출하는 메소드
    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');
        return originName.substring(index);
    }
}


