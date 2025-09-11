package org.example.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.example.product.util.ProjectConstants.*;


@Controller
@Tag(name = "Страницы", description = "Управление продуктами")
public class PageController {
    @GetMapping("/")
    @Operation(summary = "Стартовая страница (редирект на логин)")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    @Operation(summary = "Страница авторизации")
    public String loginPage() {
        return LOGIN;
    }

    @GetMapping("/register")
    @Operation(summary = "Редирект на страницу регистрации")
    public String registerPage() {
        return REGISTER;
    }

    @GetMapping("/categories")
    @Operation(summary = "Редирект на страницу категорий")
    public String categoriesPage() {
        return CATEGORY;
    }

    @GetMapping("/products")
    @Operation(summary = "Редирект на страницу продуктов")
    public String productsPage() {
        return PRODUCT;
    }
}
