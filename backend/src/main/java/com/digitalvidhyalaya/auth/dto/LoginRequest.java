package com.digitalvidhyalaya.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Login is tenant-aware, so schoolId stays part of the request even though the username
 * is globally unique. This keeps school context explicit and safer for support flows.
 */
public record LoginRequest(
        @NotBlank String schoolId,
        @NotBlank String username,
        @NotBlank String password
) {
}
