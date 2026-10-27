package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * Author: Subash
 * Created: 10/26/2026
 * File: UserSearchRequest.java
 */
@Data
public class UserSearchRequest {
    private int page = 0;
    private int size = 10;
    private String search;
    private AccountStatus status;
    private long limit;
    private int offset;

    public void prepare(Pageable pageable) {
        this.search = normalize(search);
        this.limit = pageable.getPageSize();
        this.offset = (int) pageable.getOffset();
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
