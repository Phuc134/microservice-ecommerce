package org.example.shippingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shippingservice.enums.ShippingStatus;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String orderId;
    String userId;
    LocalDateTime shippingDate;
    String trackingNumber;
    @Enumerated(EnumType.STRING)
    ShippingStatus status;

}
