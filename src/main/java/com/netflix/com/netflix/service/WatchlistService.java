package com.netflix.service;

import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.VideoResponse;

//Add your annotations here
public interface WatchlistService {
    MessageResponse addToWatchlist(String email, Long videoId);

    MessageResponse removeFromWatchlist(String email, Long videoId);

    PageResponse<VideoResponse> getWatchlistPaginated(String email, int page, int size, String search);
}
