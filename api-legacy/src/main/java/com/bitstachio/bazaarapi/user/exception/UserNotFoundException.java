package com.bitstachio.bazaarapi.user.exception;

import com.bitstachio.bazaarapi.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID id) {
        super("User not found with ID: " + id);
    }
}
