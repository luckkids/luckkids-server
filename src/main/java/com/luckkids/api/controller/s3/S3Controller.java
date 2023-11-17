package com.luckkids.api.controller.s3;

import com.luckkids.api.service.s3.S3Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable("fileName") String fileName){
        return s3Service.download(fileName);
    }

    @GetMapping("/preview/{fileName}")
    public String preview(@PathVariable("fileName") String fileName, HttpServletResponse response){
        return s3Service.preview(fileName, response);
    }
}
