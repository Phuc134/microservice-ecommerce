package org.example.authservice.repository.httpClient;

import org.example.authservice.dto.request.ProfileCreationRequest;
import org.example.authservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "profile-service", url = "http://localhost:8081/profile")
public interface ProfileClient {
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse createProfile(@RequestBody ProfileCreationRequest profileCreationRequest);

}
