package com.luckkids.api.service.s3;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class S3ServiceTest extends IntegrationTestSupport {

    @Autowired
    private S3Service s3Service;

    @Value("${s3.bucketName}")
    private String bucketName;

    @DisplayName("test.json파일을 다운로드한다.")
    @Test
    void download(){
        String fileName = "test.json";

        ResponseEntity<byte[]> responseEntity = s3Service.download(fileName);

        HttpHeaders headers = responseEntity.getHeaders();
        String contentDisposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);

        String downloadFileName = null;
        if (contentDisposition != null) {
            int fileNameIndex = contentDisposition.indexOf("filename=");
            if (fileNameIndex != -1) {
                downloadFileName = contentDisposition.substring(fileNameIndex + 10, contentDisposition.length() - 1);
            }
        }

        assertThat(downloadFileName).isEqualTo(fileName);
    }

    @DisplayName("존재하지 않는 파일을 다운로드시 에러가 발생한다.")
    @Test
    void downloadNotExistFile(){
        String fileName = "test.jon";

        assertThatThrownBy(() -> s3Service.download(fileName))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("파일 다운로드중 오류가 발생했습니다.");
    }

    @DisplayName("파일을 미리보기를 한다.")
    @Test
    void previewTest(){
        String fileName = "test.json";
        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream objectInputStream = new S3ObjectInputStream(
            new ByteArrayInputStream("test.json".getBytes(StandardCharsets.UTF_8)), null);

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, "test.json");

        given(amazonS3Client.getObject(eq(getObjectRequest)))
            .willReturn(
                s3Object
            );

        given(s3Object.getObjectContent())
            .willReturn(
                objectInputStream
            );

        HttpServletResponse response = new MockHttpServletResponse();

        String downloadFileName = s3Service.preview("test.json", response);

        assertEquals(fileName, downloadFileName);
    }
}
