package org.example.shippingservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.shippingservice.dto.request.ShippingRequest;
import org.example.shippingservice.dto.response.ShippingResponse;
import org.example.shippingservice.enums.ShippingStatus;
import org.example.shippingservice.service.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ShippingController {
    ShippingService shippingService;

    @PostMapping
    public ResponseEntity<ShippingResponse> createShipping(@RequestBody ShippingRequest shippingRequest) {
        return ResponseEntity.ok(shippingService.createShipping(shippingRequest));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ShippingResponse> updateShipmentStatus(@PathVariable String orderId,
                                                                 @RequestParam ShippingStatus newStatus) {
        ShippingResponse updatedShipment = shippingService.updateShippingStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedShipment);
    }
}
