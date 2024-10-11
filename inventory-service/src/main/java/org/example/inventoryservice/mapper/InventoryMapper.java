package org.example.inventoryservice.mapper;

import org.example.inventoryservice.dto.request.InventoryRequest;
import org.example.inventoryservice.dto.response.InventoryResponse;
import org.example.inventoryservice.entity.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryRequest request);
    InventoryResponse toInventoryResponse(Inventory inventory);
}
