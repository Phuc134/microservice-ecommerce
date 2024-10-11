package org.example.authservice.mapper;

import org.example.authservice.dto.request.UserRequest;
import org.example.authservice.dto.response.UserResponse;
import org.example.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);
}
