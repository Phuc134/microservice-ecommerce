package org.example.productservice.repository;

import org.example.productservice.entity.Product;
import org.example.productservice.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    List<ProductImage> findByProduct(Product product);
}
