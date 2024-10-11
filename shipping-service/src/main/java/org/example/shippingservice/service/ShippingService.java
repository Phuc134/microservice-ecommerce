package org.example.shippingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shippingservice.dto.request.ShippingRequest;
import org.example.shippingservice.dto.response.ShippingResponse;
import org.example.shippingservice.entity.Shipping;
import org.example.shippingservice.enums.ShippingStatus;
import org.example.shippingservice.mapper.ShippingMapper;
import org.example.shippingservice.repository.ShippingRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class ShippingService {
    ShippingRepository shippingRepository;
    ShippingMapper shippingMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    private static final String ORDER_SHIPPED_TOPIC  = "ORDER_SHIPPED";
    private static final String ORDER_DELIVERED_TOPIC = "ORDER_DELIVERED";
    public ShippingResponse createShipping(ShippingRequest shippingRequest) {
        Shipping shipping = shippingMapper.toShipping(shippingRequest);
        Shipping shippingExists = shippingRepository.findByOrderId(shipping.getOrderId()).orElse(null);
        if (shippingExists != null) {
            throw new RuntimeException("Shipment already exists for order: " + shipping.getOrderId());
        }
        shipping.setShippingDate(LocalDateTime.now());
        shipping.setStatus(ShippingStatus.SHIPPED);
        shipping.setTrackingNumber(UUID.randomUUID().toString());
        Shipping savedShipment = shippingRepository.save(shipping);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderId", savedShipment.getOrderId());
        eventData.put("userId", savedShipment.getUserId());
        eventData.put("status", ShippingStatus.SHIPPED);
        kafkaTemplate.send(ORDER_SHIPPED_TOPIC, eventData);

        return shippingMapper.toShippingResponse(savedShipment);

    }

    public ShippingResponse updateShippingStatus(String orderId, ShippingStatus newStatus) {
        Shipping shipment = shippingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipment not found for order: " + orderId));

        if (!shipment.getStatus().canChangeTo(newStatus)) {
            throw new RuntimeException("Cannot change shipment status from " + shipment.getStatus() + " to " + newStatus);
        }

        shipment.setStatus(newStatus);
        Shipping updatedShipment = shippingRepository.save(shipment);
        if (newStatus!= ShippingStatus.IN_TRANSIT) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("orderId", updatedShipment.getOrderId());
            eventData.put("userId", updatedShipment.getUserId());
            eventData.put("status", newStatus);
            kafkaTemplate.send(ORDER_DELIVERED_TOPIC , eventData);
        }
        return shippingMapper.toShippingResponse(updatedShipment);
    }

}
