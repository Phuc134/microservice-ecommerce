package org.example.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder

public class ProductResponse {
    String id;
    String name;
    String description;
    float price;
    String categoryName;
    int stockQuantity;
    List<String> imageUrls;
}
