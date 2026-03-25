package com.bitstachio.bazaarapi.user.service;

import com.bitstachio.bazaarapi.user.domain.User;
import com.bitstachio.bazaarapi.user.dto.UserCreateRequest;
import com.bitstachio.bazaarapi.user.dto.UserResponse;
import com.bitstachio.bazaarapi.user.dto.UserUpdateRequest;
import com.bitstachio.bazaarapi.user.exception.UserNotFoundException;
import com.bitstachio.bazaarapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserCreateRequest request) {
        var user = User.builder().name(request.name()).email(request.email()).build();

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getById(UUID id) {
        return userRepository
                .findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public UserResponse update(UUID id, UserUpdateRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.name());
        user.setEmail(request.email());

        return toResponse(userRepository.save(user));
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
