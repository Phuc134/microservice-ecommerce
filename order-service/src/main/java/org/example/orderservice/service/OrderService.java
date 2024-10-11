package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.orderservice.dto.request.OrderItemRequest;
import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.response.InventoryResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.dto.response.ProfileResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.enums.OrderStatus;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderItemRepository;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.repository.httpClient.InventoryClient;
import org.example.orderservice.repository.httpClient.ProfileClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemRepository orderItemRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    InventoryClient inventoryClient;
    ProfileClient profileClient;

    private static final String TOPIC = "ORDER_CREATED";

    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Check inventory for each product in the order
        for (var orderItemRequest : orderRequest.getOrderItems()) {
            InventoryResponse inventory = inventoryClient.getInventoryByProductId(orderItemRequest.getProductId());
            if (inventory.getStockQuantity() < orderItemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product " + orderItemRequest.getProductId());
            }
        }

        // Save the order without cascading the OrderItems
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .orderDate(LocalDateTime.now())
                .totalAmount(orderRequest.getTotalAmount())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // Now manually handle OrderItems
        List<OrderItem> orderItems = orderMapper.toOrderItems(orderRequest.getOrderItems(), savedOrder);
        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem);
        }

        // Optionally send Kafka events
        for (var orderItemRequest : orderRequest.getOrderItems()) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("productId", orderItemRequest.getProductId());
            eventData.put("quantity", orderItemRequest.getQuantity());
            kafkaTemplate.send(TOPIC, eventData);
        }
        OrderResponse orderResponse = orderMapper.toOrderResponse(savedOrder);
        orderResponse.setOrderItems(orderMapper.toOrderItemResponses(orderItems));
        return orderResponse;
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderItem> orderItem = orderItemRepository.findOrderItemByOrderId(orderId);
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderItems(orderMapper.toOrderItemResponses(orderItem));
        return orderResponse;
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().canChangeTo(newStatus)) {
            throw new RuntimeException("Cannot change order status from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> orderItem = orderItemRepository.findOrderItemByOrderId(orderId);
        OrderResponse orderResponse = orderMapper.toOrderResponse(updatedOrder);
        orderResponse.setOrderItems(orderMapper.toOrderItemResponses(orderItem));
        return orderResponse;
    }

    @KafkaListener(topics = {"ORDER_SHIPPED", "ORDER_DELIVERED"})
    public void handleShippingEvent(Map<String, Object> event) {
        String orderId = (String) event.get("orderId");
        String statusStr = (String) event.get("status");

        OrderStatus newStatus = OrderStatus.valueOf(statusStr);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().canChangeTo(newStatus)) {
            throw new RuntimeException("Cannot change order status from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        ProfileResponse profile = profileClient.getProfileByProfileId(order.getUserId());
        // Send email to user
        Map<String, Object> eventData = new HashMap<>();
        Map<String, Object> toData = new HashMap<>();
        toData.put("name", profile.getFirstName());
        toData.put("email", profile.getEmail());

        eventData.put("to", toData);
        if (newStatus == OrderStatus.SHIPPED) {
            eventData.put("subject", "Order Shipped");
            eventData.put("htmlContent", "Your order has been shipped");
        } else if (newStatus == OrderStatus.DELIVERED) {
            eventData.put("subject", "Order Delivered");
            eventData.put("htmlContent", "Your order has been delivered");
        }

        kafkaTemplate.send("SEND_EMAIL", eventData);
    }

}
