package org.example.orderservice.repository;

import org.example.orderservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findOrderItemByOrderId(String orderId);
}
