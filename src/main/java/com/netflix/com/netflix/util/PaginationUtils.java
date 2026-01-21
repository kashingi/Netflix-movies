package com.netflix.util;

import com.netflix.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;

//Add your annotations here
public class PaginationUtils {
    private PaginationUtils() {}

    public static Pageable createPageRequest(int page, int size, String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        }
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
    }

    public static Pageable createPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static <T, R>PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        List<R> content = page.getContent().stream().map(mapper).toList();

        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }

    public static <R> PageResponse<R> toPageResponse(Page<?> page, List<R> mappedContent) {
        return new PageResponse<>(mappedContent, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }
}
