package com.digitalvidhyalaya.auth.dto;

import com.digitalvidhyalaya.common.enums.RoleCode;
import java.util.Set;

public record LoginResponse(
        String schoolId,
        String username,
        String fullName,
        Set<RoleCode> roles,
        boolean readOnlyAccess,
        String message
) {
}
