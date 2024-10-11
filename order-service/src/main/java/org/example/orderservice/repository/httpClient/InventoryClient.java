package org.example.orderservice.repository.httpClient;

import org.example.orderservice.dto.response.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", url = "http://localhost:8084/inventory")
public interface InventoryClient {
    @GetMapping("/{productId}")
    InventoryResponse getInventoryByProductId(@PathVariable("productId") String productId);
}
