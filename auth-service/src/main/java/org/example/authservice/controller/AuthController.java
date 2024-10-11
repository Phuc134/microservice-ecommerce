package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.dto.request.AuthRequest;
import org.example.authservice.dto.request.IntrospectRequest;
import org.example.authservice.dto.request.UserRequest;
import org.example.authservice.dto.response.AuthResponse;
import org.example.authservice.dto.response.IntrospectResponse;
import org.example.authservice.dto.response.UserResponse;
import org.example.authservice.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    @PostMapping("/test")
    public String test(){
        return "test";
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws Exception {
        return authService.login(request);
    }
    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest request) throws Exception {
        return authService.register(request);
    }
    @PostMapping("/introspect")
    public IntrospectResponse introspect(@RequestBody IntrospectRequest request) throws Exception {
        return authService.introspect(request);
    }
}
