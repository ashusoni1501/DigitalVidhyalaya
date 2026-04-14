package com.digitalvidhyalaya.user.controller;

import com.digitalvidhyalaya.common.response.ApiResponse;
import com.digitalvidhyalaya.user.dto.CreateUserRequest;
import com.digitalvidhyalaya.user.dto.UpdateUserStatusRequest;
import com.digitalvidhyalaya.user.dto.UserResponse;
import com.digitalvidhyalaya.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success("User created successfully", userService.createUser(request));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsersBySchool(@RequestParam String schoolId) {
        return ApiResponse.success("Users fetched successfully", userService.getUsersBySchool(schoolId));
    }

    @PutMapping("/{userId}/status")
    public ApiResponse<UserResponse> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return ApiResponse.success("User status updated successfully", userService.updateUserStatus(userId, request));
    }
}
