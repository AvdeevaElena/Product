package org.example.product.service;

import org.example.product.dto.CategoryDto;
import org.example.product.model.Category;
import org.example.product.repository.CategoryRepository;
import org.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.product.util.СommonUtils.formatDateTime;
import static org.example.product.util.СommonUtils.getUser;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllActive() {
        return categoryRepository.findByCategoryIsDelOrderByCategoryIdAsc(0);
    }

    public boolean deactivateCategory(int categoryId) {
        boolean isCategoryUpdated = categoryRepository.updateCategoryStatus(categoryId, 1) > 0;
        if (isCategoryUpdated) {
            productRepository.updateProductIsActiveByCategory(categoryId);
        }
        return isCategoryUpdated;
    }

    public void updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById((long) categoryDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категория с таким id не найдена."));
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
    }

    public void createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        category.setCategoryIsDel(0);
        category.setCategoryCreationDate(formatDateTime());
        category.setCategoryCreationUser(getUser());
        categoryRepository.save(category);
    }
}