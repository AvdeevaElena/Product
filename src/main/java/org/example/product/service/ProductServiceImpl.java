package org.example.product.service;

import org.example.product.dto.ProductDto;
import org.example.product.model.Category;
import org.example.product.model.Product;
import org.example.product.repository.CategoryRepository;
import org.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.example.product.util.СommonUtils.formatDateTime;
import static org.example.product.util.СommonUtils.getUser;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllActive() {
        return productRepository.findByProductIsDelAndProductIsActiveOrderByProductIdAsc(0, 1);
    }

    public void updateProduct(List<ProductDto> products) {
        for (ProductDto productDto : products) {
            Product product = productRepository.findById(productDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Продукт с таким id не найден."));

            Category category = categoryRepository.findById(productDto.getCategory().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Категория с таким id не найдена."));

            product.setCategory(category);
            product.setProductPrice(BigDecimal.valueOf(productDto.getProductPrice()));
            product.setProductIsActive(productDto.getProductIsActive());

            if (category.getCategoryIsDel() != null && category.getCategoryIsDel() == 1) {
                product.setProductIsActive(0);
            }
            product.setProductName(productDto.getProductName());
            product.setProductDescription(productDto.getProductDescription());
            product.setProductModifiedUser(getUser());
            product.setProductModifiedDate(formatDateTime());
            product.setProductImageUrl(productDto.getProductImageUrl());
        }
    }

    public void createProduct(ProductDto productDto){
        Product product = new Product();
        Long categoryId = productDto.getCategory().getCategoryId().longValue();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория с таким id не найдена."));
        product.setCategory(category);
        product.setProductPrice(BigDecimal.valueOf(productDto.getProductPrice()));
        product.setProductIsActive(productDto.getProductIsActive());
        product.setProductName(productDto.getProductName());
        product.setProductDescription(productDto.getProductDescription());
        product.setProductCreationUser(getUser());
        product.setProductCreationDate(formatDateTime());
        product.setProductImageUrl(productDto.getProductImageUrl());
        product.setProductIsDel(0);
        productRepository.save(product);
    }

    public void removeProduct(int productId) {
        Product product = productRepository.findById((long)productId)
                .orElseThrow(() -> new RuntimeException("Продукт с таким id не найден."));
        product.setProductModifiedUser(getUser());
        product.setProductModifiedDate(formatDateTime());
        product.setProductIsDel(1);
    }
}
