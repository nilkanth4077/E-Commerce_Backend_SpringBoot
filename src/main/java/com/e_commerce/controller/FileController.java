package com.e_commerce.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file){
        String fileDir = "D:\\Springboot files";
        File fileFromUser = new File(fileDir);

        if(!fileFromUser.exists()){
            fileFromUser.mkdir();
        }

        String filePath = fileDir + File.separator + file.getOriginalFilename();
        String fileUploadStatus;

        try {
                file.transferTo(new File(filePath));
                fileUploadStatus = "File uploaded successfully: " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            fileUploadStatus = "File upload failed: " + e.getMessage();
        }

        return fileUploadStatus;
    }

    @GetMapping("/getAllFiles")
    public List<String> listFiles() {
        String fileDir = "D:\\Springboot files";

        File folder = new File(fileDir);
        if (folder.exists() && folder.isDirectory()) {
            return Arrays.stream(folder.listFiles())
                    .filter(File::isFile)
                    .map(File::getName)
                    .collect(Collectors.toList());
        } else {
            return Arrays.asList("No files found or directory does not exist.");
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileName) {
        String fileDir = "D:\\Springboot files";

        String filePath = fileDir + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            return new ResponseEntity<>("File named: '" + file.getName() + "' not found", HttpStatus.NOT_FOUND);
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while downloading the file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
