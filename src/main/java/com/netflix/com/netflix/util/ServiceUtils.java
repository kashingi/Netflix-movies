package com.netflix.util;

import com.netflix.dao.UserRepository;
import com.netflix.dao.VideoRepository;
import com.netflix.entity.User;
import com.netflix.entity.Video;
import com.netflix.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Add your annotations here
@Component
public class ServiceUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email : " + email));
    }

    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : " + id));
    }

    public Video getVideoByIdOrThrow(Long id) {
        return videoRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Video not found with id : " + id));
    }
}
