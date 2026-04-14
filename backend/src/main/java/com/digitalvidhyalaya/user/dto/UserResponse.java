package com.digitalvidhyalaya.user.dto;

import com.digitalvidhyalaya.common.enums.RoleCode;
import com.digitalvidhyalaya.common.enums.UserStatus;
import java.time.OffsetDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String schoolId,
        String username,
        String fullName,
        String email,
        String phone,
        UserStatus status,
        Set<RoleCode> roles,
        OffsetDateTime lastLoginAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
