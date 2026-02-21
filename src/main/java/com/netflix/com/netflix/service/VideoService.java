package com.netflix.service;

import com.netflix.dto.request.VideoRequest;
import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.VideoResponse;
import com.netflix.dto.response.VideoStatsResponse;
import jakarta.validation.Valid;

import java.util.List;

//Add your annotations here
public interface VideoService {
    MessageResponse createVideoByAdmin(VideoRequest videoRequest);

    PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search);

    MessageResponse updateByAdmin(Long id, @Valid VideoRequest videoRequest);

    MessageResponse deleteVideoByAdmin(Long id);

    MessageResponse toggleVideoStatus(Long id, boolean value);

    VideoStatsResponse getAdminStats();

    PageResponse<VideoResponse> getPublishedVideos(int page, int size, String search, String email);

    List<VideoResponse> getFeaturedVideos();
}
