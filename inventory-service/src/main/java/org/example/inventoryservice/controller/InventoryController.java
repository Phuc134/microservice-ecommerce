package org.example.inventoryservice.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.inventoryservice.dto.request.InventoryRequest;
import org.example.inventoryservice.dto.response.InventoryResponse;
import org.example.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryController {
    InventoryService inventoryService;
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable String productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> addInventory(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.addInventory(request));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable String productId,
                                                             @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(productId, request));
    }
}
