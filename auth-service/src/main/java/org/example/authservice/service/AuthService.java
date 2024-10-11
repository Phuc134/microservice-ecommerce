package org.example.authservice.service;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.configuration.JwtUtil;
import org.example.authservice.dto.request.AuthRequest;
import org.example.authservice.dto.request.IntrospectRequest;
import org.example.authservice.dto.request.UserRequest;
import org.example.authservice.dto.response.AuthResponse;
import org.example.authservice.dto.response.IntrospectResponse;
import org.example.authservice.dto.response.ProfileResponse;
import org.example.authservice.dto.response.UserResponse;
import org.example.authservice.entity.User;
import org.example.authservice.mapper.ProfileMapper;
import org.example.authservice.mapper.UserMapper;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.repository.httpClient.ProfileClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    ProfileClient profileClient;
    public AuthResponse login(AuthRequest request) throws JOSEException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new RuntimeException("Invalid password");
        }
        var token = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        boolean valid = jwtUtil.introspect(request.getToken());
        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    public UserResponse register(UserRequest request) {
        User user = userMapper.toUser(request);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);
        user = userRepository.save(user);
        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());
        ProfileResponse profileResponse = profileClient.createProfile(profileRequest);
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setFirstName(profileResponse.getFirstName());
        userResponse.setLastName(profileResponse.getLastName());
        userResponse.setCity(profileResponse.getCity());
        userResponse.setDob(profileResponse.getDob());
        userResponse.setEmail(profileResponse.getEmail());
        return  userResponse;
    }
}
