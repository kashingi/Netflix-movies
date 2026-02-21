package com.netflix.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

//Add your annotations here
public interface FileUploadService {
    String storeVideoFile(MultipartFile file);

    String storeImageFile(MultipartFile file);

    ResponseEntity<Resource> serveVideo(String uuid, String rangeHeader);

    ResponseEntity<Resource> serveImage(String uuid);
}
