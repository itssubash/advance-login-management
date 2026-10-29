package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.dtos.GlobalResponse;
import com.yenyasoft.admin_user_management.dtos.LoginHistoryResponse;
import com.yenyasoft.admin_user_management.dtos.LoginHistorySearchRequest;
import com.yenyasoft.admin_user_management.mapper.LoginHistoryMapper;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import com.yenyasoft.admin_user_management.repository.LoginHistoryRepository;
import com.yenyasoft.admin_user_management.service.LoginHistoryService;
import com.yenyasoft.admin_user_management.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Subash
 * Created: 10/13/2026
 * File: LoginHistoryServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryMapper loginHistoryMapper;

    @Override
    public void saveLoginHistory(LoginHistory loginHistory) {
        loginHistoryRepository.save(loginHistory);
    }

    @Override
    public GlobalResponse<Page<LoginHistoryResponse>> getMyLoginHistory(LoginHistorySearchRequest request) {
        return search(request, "Your login history retrieved successfully");
    }


    @Override
    public GlobalResponse<Page<LoginHistoryResponse>> getAllLoginHistory(LoginHistorySearchRequest request) {
        return search(request, "Login History retrieved successfully");
    }

    private GlobalResponse<Page<LoginHistoryResponse>> search(
            LoginHistorySearchRequest request,
            String message) {

        Pageable pageable = PagingUtils.createPageable(
                request.getPage(), request.getSize(), Sort.Direction.DESC, "loggedInAt");
        request.prepare(pageable);

        List<LoginHistoryResponse> records = loginHistoryMapper
                .searchHistoryPaged(request)
                .stream()
                .map(LoginHistoryResponse::toResponse)
                .toList();

        long totalElements = loginHistoryMapper.countSearchHistory(request);

        return GlobalResponse.success(
                message,
                PagingUtils.toPage(records, totalElements, pageable)
        );
    }
}
