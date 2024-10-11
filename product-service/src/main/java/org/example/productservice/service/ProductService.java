package org.example.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.productservice.dto.request.ProductRequest;
import org.example.productservice.dto.response.ProductResponse;
import org.example.productservice.entity.Category;
import org.example.productservice.entity.Product;
import org.example.productservice.entity.ProductImage;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.repository.CategoryRepository;
import org.example.productservice.repository.ProductImageRepository;
import org.example.productservice.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductImageRepository productImageRepository;
    ProductMapper productMapper;
    KafkaTemplate<String, Object> kafkaTemplate;
    private List<String> mapImages(List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getImageUrl)
                .toList();
    }

    public ProductResponse createProduct(ProductRequest productRequest, List<MultipartFile> images) throws IOException {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);
        Product productSaved = productRepository.save(product);

        List<ProductImage> productImages = images.stream().map(file -> {
            try {
                String imageUrl = saveImage(file);
                return ProductImage.builder()
                        .imageUrl(imageUrl)
                        .product(productSaved)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }).toList();

        productImageRepository.saveAll(productImages);



        ProductResponse productResponse = productMapper.toResponse(productSaved);
        productResponse.setCategoryName(category.getName());
        productResponse.setImageUrls(mapImages(productImages));

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("productId", productSaved.getId());
        eventData.put("quantity", productSaved.getStockQuantity());
        kafkaTemplate.send("CREATE_PRODUCT", eventData);

        return productResponse;
    }

    private String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Path.of("product-service/uploads", fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return path.toString();
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        product = productRepository.save(product);
        ProductResponse productResponse = productMapper.toResponse(product);
        productResponse.setCategoryName(category.getName());
        productResponse.setImageUrls(mapImages(productImageRepository.findByProduct(product)));
        return productResponse;
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        productImageRepository.deleteAll(productImages);
        productRepository.delete(product);
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductImage> images = productImageRepository.findByProduct(product);
        ProductResponse productResponse = productMapper.toResponseWithImages(product, images);
        productResponse.setCategoryName(product.getCategory().getName());
        productResponse.setImageUrls(mapImages(images));
        return productResponse;
    }

    public List<ProductResponse> getProductsByCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        kafkaTemplate.send("onboard-successful", "All products fetched successfully");
        return products.stream()
                .map(product -> {
                    List<ProductImage> images = productImageRepository.findByProduct(product);
                    ProductResponse productResponse = productMapper.toResponseWithImages(product, images);
                    productResponse.setCategoryName(product.getCategory().getName());
                    productResponse.setImageUrls(mapImages(images));
                    return productResponse;
                })
                .collect(Collectors.toList());
    }

}
