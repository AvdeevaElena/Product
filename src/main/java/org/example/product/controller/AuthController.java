package org.example.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.product.dto.UserDto;
import org.example.product.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

import static org.example.product.util.ProjectConstants.*;

@RestController
@Tag(name = "Аутентификация", description = "Регистрация и авторизация пользователей")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Зарегистроваться")
    public Map<String, Object> register(@RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        if (userDto.getUsername() == null || userDto.getPassword() == null) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Не заполнена форма регистрации.");
        }
        try {
            userService.registrateUser(userDto);
            response.put(SUCCESS, true);
            response.put(MESSAGE, "Пользователь зарегистрировался!");
        } catch (Exception e) {
            response.put(SUCCESS, false);
        }
        return response;
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизироваться с ключом")
    public Map<String, Object> login(@RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        if (userDto.getUsername() == null || userDto.getPassword() == null) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Не заполнена форма авторизации.");
            return response;
        }
        try {
            response = userService.authUser(userDto);
            response.put(SUCCESS, true);
            response.put(MESSAGE, "Пользователь авторизировался!");

        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при авторизации." + e.getMessage());
        }
        return response;
    }
}
