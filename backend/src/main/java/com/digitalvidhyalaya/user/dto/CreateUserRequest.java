package com.digitalvidhyalaya.user.dto;

import com.digitalvidhyalaya.common.enums.RoleCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Creates a global user identity and assigns one or more roles within a school.
 */
public record CreateUserRequest(
        @NotBlank String schoolId,
        @NotBlank @Size(max = 100) String username,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Size(max = 150) String fullName,
        @Email @Size(max = 150) String email,
        @Size(max = 20) String phone,
        @NotEmpty Set<RoleCode> roles
) {
}
