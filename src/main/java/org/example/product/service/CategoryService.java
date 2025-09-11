package org.example.product.service;

import org.example.product.dto.CategoryDto;
import org.example.product.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllActive();
    boolean deactivateCategory(int categoryId);
    void updateCategory(CategoryDto categoryDto);
    void createCategory(CategoryDto categoryDto);
}
