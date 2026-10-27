package com.yenyasoft.admin_user_management.exceptions;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AlreadyExistsException.java
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
