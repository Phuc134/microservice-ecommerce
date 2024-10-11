package org.example.productservice.repository;

import org.example.productservice.entity.Category;
import org.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCategory(Category category);
}
