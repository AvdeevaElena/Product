package org.example.product.util;

import org.example.product.dto.CategoryDto;
import org.example.product.dto.ProductDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class СommonUtils {
    public static String formatDateTime() {
        LocalDateTime todayDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return todayDate.format(formatter);
    }

    public static String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "ADMIN";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }

    public static String validateCategoryForUpdate(CategoryDto categoryDto) {
        String validation = validateCategoryForCreate(categoryDto);
        if (validation != null) {
            return validation;
        }
        if (categoryDto.getCategoryId() == null) {
            return "ID категории обязателен. ";
        }
        return null;
    }

    public static String validateCategoryForCreate(CategoryDto categoryDto) {
        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isBlank()) {
            return "Название категории не должно быть пустым. ";
        }
        if (categoryDto.getCategoryDescription() == null || categoryDto.getCategoryDescription().isBlank()) {
            return "Описание категории не должно быть пустым. ";
        }
        return null;
    }

    public static String validateProduct(List<ProductDto> products) {
        StringBuilder sb = new StringBuilder();
        for (ProductDto product : products) {
            if (product.getProductId() == null) {
                sb.append("Продукт id не заполнен. ");
            }
            sb.append(validateProduct(product));
        }
        return sb.toString();
    }

    public static String validateProduct(ProductDto product) {
        StringBuilder sb = new StringBuilder();
        sb.append(validateCommon(product));

        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty()
                && !product.getProductImageUrl().toLowerCase().startsWith("http://")) {
            sb.append("Image URL заполнено не корректно. ");
        }
        return sb.toString();
    }

    private static String validateCommon(ProductDto product) {
        StringBuilder sb = new StringBuilder();
        if (product.getProductIsActive() == null) {
            sb.append("Активность не заполнена. ");
        }
        if (product.getCategory() == null || product.getCategory().getCategoryId() == null) {
            sb.append("Категория не заполнена. ");
        }
        if (product.getProductPrice() == null || product.getProductPrice() <= 0) {
            sb.append("Стоимость не заполнена. ");
        }
        if (product.getProductDescription() == null || product.getProductDescription().isEmpty()) {
            sb.append("Описание не заполнено. ");
        }
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            sb.append("Название не заполнено. ");
        }
        return sb.toString();
    }
}
