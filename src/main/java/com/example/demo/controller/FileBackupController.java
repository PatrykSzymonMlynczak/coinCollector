package com.example.demo.controller;

import com.example.demo.fileManager.exception.JsonFileNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileBackupController {

    @ApiOperation(value = "Endpoint allowing to download sales file in JSON format")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully downloaded all sales"),
            @ApiResponse(code = 400, message = "Bad request")})
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

    @ApiOperation(value = "Endpoint allowing to upload sale file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully downloaded all sales"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping("/upload/{fileName}")
    public String handleFileUpload(@PathVariable("fileName") String fileName,
                                      @RequestParam("file") MultipartFile file) {
            String uploadedJsonFile = "";

            try (FileWriter fileWriter = new FileWriter(fileName+".json", false)){
                uploadedJsonFile = new String(file.getBytes());
                fileWriter.write(uploadedJsonFile);
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return uploadedJsonFile;
    }

}