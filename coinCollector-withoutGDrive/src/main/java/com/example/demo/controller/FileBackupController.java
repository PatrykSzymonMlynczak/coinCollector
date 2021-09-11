package com.example.demo.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.demo.exception.JsonFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileBackupController {


    @GetMapping("/download/{fileName}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        Path path = Paths.get(fileName+".json");

        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (!resource.exists()) throw new JsonFileNotFoundException(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping("/upload/{fileName}")
    public String handleFileUpload(@PathVariable("fileName") String fileName,
                                      @RequestParam("file") MultipartFile file) {
            String uploadedJsonFile = "";
            FileWriter fileWriter;

            try {
                uploadedJsonFile = new String(file.getBytes());
                fileWriter = new FileWriter(fileName+".json", false);
                fileWriter.write(uploadedJsonFile);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return uploadedJsonFile;

    }

}