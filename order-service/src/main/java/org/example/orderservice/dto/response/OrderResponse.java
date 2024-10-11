package org.example.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String userId;
    LocalDateTime orderDate;
    float totalAmount;
    String status;
    List<OrderItemResponse> orderItems;
}
