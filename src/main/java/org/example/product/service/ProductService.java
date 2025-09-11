package org.example.product.service;

import org.example.product.dto.ProductDto;
import org.example.product.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllActive();
    void updateProduct(List<ProductDto> products);
    void createProduct(ProductDto product);
    void removeProduct(int productId);
}
