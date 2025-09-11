package org.example.product.service;

import org.example.product.dto.UserDto;

import java.util.Map;

public interface UserService {
    Map<String, Object> authUser(UserDto userDto);
    void registrateUser(UserDto userDto);
}
