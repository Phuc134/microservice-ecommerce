package org.example.shippingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ShippingResponse {
    String id;
    String orderId;
    String userId;
    String trackingNumber;
    LocalDateTime shippingDate;
    String status;
}
