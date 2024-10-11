package org.example.productservice.mapper;

import org.example.productservice.dto.request.ProductRequest;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.entity.Product;
import org.example.productservice.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")

public interface ProductMapper {
    Product toProduct(ProductRequest productRequest);

    ProductResponse toResponse(Product product);


    ProductResponse toResponseWithImages(Product product, List<ProductImage> images);
}
