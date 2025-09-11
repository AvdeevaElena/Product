package org.example.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.product.dto.CategoryDto;
import org.example.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.example.product.util.ProjectConstants.*;
import static org.example.product.util.СommonUtils.validateCategoryForCreate;
import static org.example.product.util.СommonUtils.validateCategoryForUpdate;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Категории", description = "Управление категориями")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping()
    @Operation(summary = "Получить все неудаленные категории")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<String, Object> getAllActiveCategories() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put(CATEGORY, categoryService.getAllActive());
            response.put(SUCCESS, true);
            return response;
        } catch (Exception ex) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при получении категории.");
        }
        return response;
    }

    @PutMapping("/update")
    @Operation(summary = "Обновить категорию")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновить категорию", required = true
            )
            @RequestBody CategoryDto categoryDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String validationError = validateCategoryForUpdate(categoryDto);
            if (validationError != null) {
                response.put(SUCCESS, false);
                response.put(MESSAGE, validationError);
                return response;
            }
            categoryService.updateCategory(categoryDto);
            response.put(SUCCESS, true);
            response.put(MESSAGE, "Категория изменена.");
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при изменении категории.");
        }
        return response;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать категорию")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные новой категории", required = true
            )
            @RequestBody CategoryDto categoryDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String validationError = validateCategoryForCreate(categoryDto);
            if (validationError != null) {
                response.put(SUCCESS, false);
                response.put(MESSAGE, validationError);
                return response;
            }
            categoryService.createCategory(categoryDto);
            response.put(SUCCESS, true);
            response.put(MESSAGE, "Категория создана.");
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при создании категории.");
        }
        return response;
    }

    @RequestMapping("/remove")
    @Operation(summary = "Удалить категорию")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> removeCategory(
            @Parameter(description = "ID категории для удаления", required = true)
            @RequestParam int categoryId) {
        Map<String, Object> response = new HashMap<>();
        try {
            categoryService.deactivateCategory(categoryId);
            response.put(MESSAGE, "Категория удалена.");
            response.put(SUCCESS, true);
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при удалении категории.");
        }
        return response;
    }
}
