package org.example.orderservice.repository.httpClient;

import org.example.orderservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profile")

public interface ProfileClient {
    @GetMapping("/{profileId}")
    ProfileResponse getProfileByProfileId(@PathVariable("profileId") String profileId);
}
