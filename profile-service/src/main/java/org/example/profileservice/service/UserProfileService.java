package org.example.profileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.profileservice.dto.request.UserProfileRequest;
import org.example.profileservice.dto.response.UserProfileResponse;
import org.example.profileservice.mapper.UserProfileMapper;
import org.example.profileservice.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userMapper;
    public UserProfileResponse createUser(UserProfileRequest userProfileRequest) {
        var user = userMapper.toUser(userProfileRequest);
        user = userProfileRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserProfileResponse getUser(String id) {
        var user = userProfileRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    public List<UserProfileResponse> getUsers() {
        return userProfileRepository.findAll().stream().map(userMapper::toResponse).toList();
    }
}
