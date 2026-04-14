package com.digitalvidhyalaya.auth.controller;

import com.digitalvidhyalaya.auth.dto.LoginRequest;
import com.digitalvidhyalaya.auth.dto.LoginResponse;
import com.digitalvidhyalaya.auth.service.AuthService;
import com.digitalvidhyalaya.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("Login processed successfully", authService.login(request));
    }
}
