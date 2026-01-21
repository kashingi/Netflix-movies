package com.netflix.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

//Add your annotations here
@Data
public class VideoRequest {

    @NotNull(message = "Title is required")
    private String title;

    @Size(max = 4000, message = "Description must not exceed 4000 characters")
    private String description;

    private Integer year;
    private String rating;
    private String duration;
    private String src;
    private String poster;
    private String published;
    private List<String> categories;
}
