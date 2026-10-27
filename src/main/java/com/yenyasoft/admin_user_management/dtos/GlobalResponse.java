package com.yenyasoft.admin_user_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: GlobalResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalResponse <T>{
    private String message;
    private Boolean success;
    private T data;

    // success
    public static <T> GlobalResponse<T> success(String message,T data){
        return GlobalResponse.<T>builder()
                .success(true).
                message(message)
                .data(data)
                .build();
    }

    // error
    public static <T> GlobalResponse<T> error(String message,T data){
        return GlobalResponse.<T>builder()
                .success(false).
                message(message)
                .data(null)
                .build();
    }

}
