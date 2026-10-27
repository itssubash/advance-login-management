package com.yenyasoft.admin_user_management.controller;

import com.yenyasoft.admin_user_management.dtos.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: BaseController.java
 */
public abstract class BaseController {
    protected <T>ResponseEntity<GlobalResponse<T>> success(T data, String message){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(message, data));
    }
    protected <T>ResponseEntity<GlobalResponse<T>> created(T data, String message){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(message, data));
    }
    protected <T> ResponseEntity<GlobalResponse<T>> failure(T data, String message, HttpStatus status){
        return ResponseEntity
                .status(status)
                .body(GlobalResponse.error(message, data));
    }
}
