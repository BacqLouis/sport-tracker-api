package com.basespringboot.service;

import com.basespringboot.dto.UserDto;
import com.basespringboot.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserDto userDto);

    List<UserResponseDto> getAllUsers(String department);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}