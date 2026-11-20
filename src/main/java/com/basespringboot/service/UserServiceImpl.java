package com.basespringboot.service;

import com.basespringboot.dto.UserDto;
import com.basespringboot.dto.UserResponseDto;
import com.basespringboot.entity.User;
import com.basespringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserDto userDto) {
        validateUserDto(userDto);

        String normalizedEmail = userDto.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User user = new User();
        user.setUsername(userDto.getUsername().trim());
        user.setEmail(normalizedEmail);
        user.setDepartment(userDto.getDepartment().trim());

        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(String department) {
        List<User> users;
        if (department != null && !department.isBlank()) {
            users = userRepository.findByDepartmentIgnoreCase(department.trim());
        } else {
            users = userRepository.findAll();
        }
        return users.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = findUserEntityById(id);
        return mapToResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserDto userDto) {
        validateUserDto(userDto);
        User existingUser = findUserEntityById(id);

        String normalizedEmail = userDto.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailAndIdNot(normalizedEmail, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use by another user");
        }

        existingUser.setUsername(userDto.getUsername().trim());
        existingUser.setEmail(normalizedEmail);
        existingUser.setDepartment(userDto.getDepartment().trim());

        User updatedUser = userRepository.save(existingUser);
        return mapToResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = findUserEntityById(id);
        userRepository.delete(existingUser);
    }

    private User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void validateUserDto(UserDto dto) {
        if (dto == null
                || dto.getUsername() == null || dto.getUsername().isBlank()
                || dto.getEmail() == null || dto.getEmail().isBlank()
                || !dto.getEmail().contains("@")
                || dto.getDepartment() == null || dto.getDepartment().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Valid username, email and department are required");
        }
    }

    private UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDepartment());
    }
}