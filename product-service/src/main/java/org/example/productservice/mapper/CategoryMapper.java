package org.example.productservice.mapper;

import org.example.productservice.dto.request.CategoryRequest;
import org.example.productservice.dto.response.CategoryResponse;
import org.example.productservice.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
    CategoryResponse toCategoryResponse(Category category);
}
