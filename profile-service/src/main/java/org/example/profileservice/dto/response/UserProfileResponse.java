package org.example.profileservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
    String email;

}
