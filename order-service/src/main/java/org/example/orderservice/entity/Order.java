package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.orderservice.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order_table`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userId;
    LocalDateTime orderDate;
    float totalAmount;
    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;
}
