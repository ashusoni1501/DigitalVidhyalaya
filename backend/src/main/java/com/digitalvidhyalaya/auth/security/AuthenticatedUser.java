package com.digitalvidhyalaya.auth.security;

import com.digitalvidhyalaya.common.enums.RoleCode;
import java.util.Set;

/**
 * Minimal authenticated principal used by the auth module until token/session handling
 * is introduced in a later slice.
 */
public record AuthenticatedUser(
        Long userId,
        Long schoolInternalId,
        String schoolId,
        String username,
        String fullName,
        Set<RoleCode> roles,
        boolean readOnlyAccess
) {
}
