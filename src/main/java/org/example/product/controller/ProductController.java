package org.example.product.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.product.dto.ProductDto;
import org.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.product.util.ProjectConstants.*;
import static org.example.product.util.СommonUtils.validateProduct;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Продукты", description = "Управление продуктами")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    @Operation(summary = "Список всех продуктов")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<String, Object> getAllActiveProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put(PRODUCT, productService.getAllActive());
            response.put(SUCCESS, true);
            return response;
        } catch (Exception ex) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при получении продуктов.");
        }
        return response;
    }

    @PutMapping("/update")
    @Operation(summary = "Обновить продукты")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные обновляемых продуктов", required = true
            )
            @RequestBody List<ProductDto> products) {;
        Map<String, Object> response = new HashMap<>();
        String error = validateProduct(products);
        if (!error.isEmpty()) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, error);
            return response;
        }
        try {
            productService.updateProduct(products);
            response.put(SUCCESS, true);
            response.put(MESSAGE, "Продукт(ы) изменен(ы).");

        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при изменении продукта.");
        }
        return response;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать продукт")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового продукта", required = true
            )
            @RequestBody ProductDto product) {
        Map<String, Object> response = new HashMap<>();
        String error = validateProduct(product);
        if (!error.isEmpty()) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, error);
            return response;
        }
        try {
            productService.createProduct(product);
            response.put(SUCCESS, true);
            response.put(MESSAGE, "Продукт создан.");

        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при создании продукта.");
        }
        return response;
    }

    @RequestMapping("/remove")
    @Operation(summary = "Удалить продукт")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> removeProduct(
            @Parameter(description = "ID категории для удаления", required = true)
            @RequestParam int productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.removeProduct(productId);
            response.put(MESSAGE, "Продукт удален.");
            response.put(SUCCESS, true);
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при удалении продукта.");
        }
        return response;
    }
}
