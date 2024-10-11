package org.example.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.inventoryservice.dto.request.InventoryRequest;
import org.example.inventoryservice.dto.response.InventoryResponse;
import org.example.inventoryservice.entity.Inventory;
import org.example.inventoryservice.mapper.InventoryMapper;
import org.example.inventoryservice.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {
    InventoryRepository inventoryRepository;
    InventoryMapper inventoryMapper;

    @KafkaListener(topics = "CREATE_PRODUCT")
    public void handle_create_product(Map<String, Object> event) {
        try{
            String productId = (String) event.get("productId");
            var stockQuantity = (int) event.get("quantity");
            Inventory inventory = Inventory.builder()
                    .productId(productId)
                    .stockQuantity(stockQuantity)
                    .build();
            inventoryRepository.save(inventory);
        }
        catch (Exception e) {
            log.error("Failed to handle event", e);
        }
    }
    @KafkaListener(topics = "ORDER_CREATED")
    public void handle_order_created(Map<String, Object> event) {
        try {

                String productId = (String) event.get("productId");
                var quantity = (int) event.get("quantity");
                Inventory inventory = inventoryRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("Product inventory not found"));
                inventory.setStockQuantity(inventory.getStockQuantity() - quantity);
                inventoryRepository.save(inventory);

        } catch (Exception e) {
            log.error("Failed to handle event", e);
        }
    }
    public InventoryResponse getInventoryByProductId(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product inventory not found"));
        return inventoryMapper.toInventoryResponse(inventory);
    }

    public InventoryResponse updateInventory(String productId, InventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product inventory not found"));

        inventory.setStockQuantity(inventory.getStockQuantity() + request.getStockQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toInventoryResponse(updatedInventory);
    }

    public InventoryResponse addInventory(InventoryRequest request) {
        Inventory inventory = inventoryMapper.toInventory(request);
        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toInventoryResponse(savedInventory);
    }
}
