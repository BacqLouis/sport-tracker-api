package com.basespringboot.service;

import com.basespringboot.dto.UserRequest;
import com.basespringboot.dto.UserResponse;
import com.basespringboot.entity.User;
import com.basespringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse create(UserRequest request) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return toResponse(userRepository.save(user));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return toResponse(getUserOrThrow(id));
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = getUserOrThrow(id);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        userRepository.delete(getUserOrThrow(id));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User " + id + " not found"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail());
    }
}