package com.netflix.controller;

import com.netflix.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

//Add your annotations here
@RestController
@RequestMapping(path = "/api/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(path = "/upload/video")
    public ResponseEntity<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file) {
        String uuid = fileUploadService.storeVideoFile(file);

        return ResponseEntity.ok(buildUploadResponse(uuid, file));
    }

    @PostMapping(path = "/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String uuid = fileUploadService.storeImageFile(file);

        return ResponseEntity.ok(buildUploadResponse(uuid, file));
    }

    private Map<String, String> buildUploadResponse(String uuid, MultipartFile file) {

        Map<String, String> response = new HashMap<>();
        response.put("uuid", uuid);
        response.put("filename", file.getOriginalFilename());
        response.put("size", String.valueOf(file.getSize()));

        return response;
    }

    @GetMapping(path = "/video/{uuid}")
    public ResponseEntity<Resource> serveVideo(@PathVariable String uuid, @RequestHeader(value = "Range", required = false) String rangeHeader, @RequestHeader(value = "token", required = false) String tokenParam) {
        return fileUploadService.serveVideo(uuid, rangeHeader);
    }

    @GetMapping(path = "/image/{uuid}")
    public ResponseEntity<Resource> serveImage(@PathVariable String uuid) {
        return fileUploadService.serveImage(uuid);
    }
}
