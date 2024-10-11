package org.example.orderservice.mapper;

import org.example.orderservice.dto.request.OrderItemRequest;
import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface OrderMapper {
    @Mapping(target = "orderItems", ignore = true)
    OrderResponse toOrderResponse(Order order);

    default List<OrderItem> toOrderItems(List<OrderItemRequest> requests, Order order) {
        return requests.stream()
                .map(request -> {
                    OrderItem item = new OrderItem();
                    item.setQuantity(request.getQuantity());
                    item.setProductId(request.getProductId());
                    item.setPrice(request.getPrice());
                    item.setOrder(order);
                    return item;
                })
                .toList();
    }

    default List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    OrderItemResponse response = new OrderItemResponse();
                    response.setProductId(item.getProductId());
                    response.setQuantity(item.getQuantity());
                    response.setPrice(item.getPrice());
                    return response;
                })
                .toList();
    }

}
