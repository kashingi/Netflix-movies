package com.netflix.dao;

import com.netflix.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

//Add your annotations here
public interface VideoRepository extends JpaRepository<Video, Long> {
}
