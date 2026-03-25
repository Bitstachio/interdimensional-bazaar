package com.bitstachio.bazaarapi.user.service;

import com.bitstachio.bazaarapi.user.dto.UserCreateRequest;
import com.bitstachio.bazaarapi.user.dto.UserResponse;
import com.bitstachio.bazaarapi.user.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse getById(UUID id);

    List<UserResponse> getAll();

    UserResponse update(UUID id, UserUpdateRequest request);

    void delete(UUID id);
}
