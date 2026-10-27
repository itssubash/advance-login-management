package com.yenyasoft.admin_user_management.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: PagingUtils.java
 */
public final class PagingUtils {

    private PagingUtils() {
    }


    public static Pageable createPageable(int page, int size, Sort.Direction direction, String sortBy) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number cannot be negative");
        }
        if (size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be between 1 and 100");
        }
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }


    public static <T> Page<T> toPage(List<T> content, long totalElements, Pageable pageable) {
        return new PageImpl<>(content, pageable, totalElements);
    }
}
