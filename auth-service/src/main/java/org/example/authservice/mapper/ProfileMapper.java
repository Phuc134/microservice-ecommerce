package org.example.authservice.mapper;

import org.example.authservice.dto.request.ProfileCreationRequest;
import org.example.authservice.dto.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserRequest request);
}
