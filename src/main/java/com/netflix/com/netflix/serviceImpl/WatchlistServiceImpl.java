package com.netflix.serviceImpl;

import com.netflix.dao.UserRepository;
import com.netflix.dao.VideoRepository;
import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.VideoResponse;
import com.netflix.entity.User;
import com.netflix.entity.Video;
import com.netflix.service.WatchlistService;
import com.netflix.util.PaginationUtils;
import com.netflix.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

//Add your annotations here
@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ServiceUtils serviceUtils;

    @Override
    public MessageResponse addToWatchlist(String email, Long videoId) {

        User user = serviceUtils.getUserByEmailOrThrow(email);
        Video video = serviceUtils.getVideoByIdOrThrow(videoId);

        user.addToWatchlist(video);
        userRepository.save(user);

        return new MessageResponse("Video added to watchlist successfully.");
    }

    @Override
    public MessageResponse removeFromWatchlist(String email, Long videoId) {

        User user = serviceUtils.getUserByEmailOrThrow(email);
        Video video = serviceUtils.getVideoByIdOrThrow(videoId);

        user.removeFromWatchlist(video);
        userRepository.save(user);

        return new MessageResponse("Video removed from watchlist successfully");
    }

    @Override
    public PageResponse<VideoResponse> getWatchlistPaginated(String email, int page, int size, String search) {

        User user = serviceUtils.getUserByEmailOrThrow(email);
        Pageable pageable = PaginationUtils.createPageRequest(page, size);
        Page<Video> videoPage;

        if (search != null && !search.trim().isEmpty()) {
            videoPage = userRepository.searchWatchlistByUserId(user.getId(), search.trim(), pageable);
        } else {
            videoPage = userRepository.findWatchlistByUserId(user.getId(), pageable);
        }

        return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
    }
}
