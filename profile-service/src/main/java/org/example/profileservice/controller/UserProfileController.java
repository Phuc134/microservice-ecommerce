package org.example.profileservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.profileservice.dto.request.UserProfileRequest;
import org.example.profileservice.dto.response.UserProfileResponse;
import org.example.profileservice.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;
    @PostMapping
    public UserProfileResponse createUser(@RequestBody UserProfileRequest user) {
        return userProfileService.createUser(user);
    }
    @GetMapping("/{id}")
    public UserProfileResponse getUser(@PathVariable String id) {
        return userProfileService.getUser(id);
    }

    @GetMapping
    public List<UserProfileResponse> getUsers() {
        return userProfileService.getUsers();
    }
}
