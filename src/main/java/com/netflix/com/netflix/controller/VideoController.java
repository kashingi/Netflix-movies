package com.netflix.controller;

import com.netflix.dto.request.VideoRequest;
import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.VideoResponse;
import com.netflix.dto.response.VideoStatsResponse;
import com.netflix.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotations here
@RestController
@RequestMapping(path = "/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/admin")
    public ResponseEntity<MessageResponse> createVideoByAdmin(@Valid @RequestBody VideoRequest videoRequest) {
        return ResponseEntity.ok(videoService.createVideoByAdmin(videoRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin/getVideos")
    public ResponseEntity<PageResponse<VideoResponse>> getAllAdminVideos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        return ResponseEntity.ok(videoService.getAllAdminVideos(page, size, search));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/admin/updateVideo/{id}")
    public ResponseEntity<MessageResponse> updateVideoByAdmin(@PathVariable Long id, @Valid @RequestBody VideoRequest videoRequest) {
        return ResponseEntity.ok(videoService.updateByAdmin(id, videoRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/admin/deleteById/{id}")
    public ResponseEntity<MessageResponse> deleteVideoByAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.deleteVideoByAdmin(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/admin/{id}/changeStatus")
    public ResponseEntity<MessageResponse> toggleVideoStatus(@PathVariable Long id, @RequestParam boolean value) {
        return ResponseEntity.ok(videoService.toggleVideoStatus(id, value));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin/stats")
    public ResponseEntity<VideoStatsResponse> getAdminStats() {
        return ResponseEntity.ok(videoService.getAdminStats());
    }

    @GetMapping(path = "/getPublished")
    public ResponseEntity<PageResponse<VideoResponse>> getPublishedVideos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search, Authentication authentication) {
        String email = authentication.getName();
        PageResponse<VideoResponse> response = videoService.getPublishedVideos(page, size, search, email);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/featured")
    public ResponseEntity<List<VideoResponse>> getFeaturedVideos() {

        List<VideoResponse> response = videoService.getFeaturedVideos();

        return ResponseEntity.ok(response);
    }
}
