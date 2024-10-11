package org.example.profileservice.mapper;

import org.example.profileservice.dto.request.UserProfileRequest;
import org.example.profileservice.dto.response.UserProfileResponse;
import org.example.profileservice.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserProfileMapper {
    UserProfile toUser(UserProfileRequest userProfileRequest);
    UserProfileResponse toResponse(UserProfile userProfile);
}
