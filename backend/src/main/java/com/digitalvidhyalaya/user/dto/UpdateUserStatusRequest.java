package com.digitalvidhyalaya.user.dto;

import com.digitalvidhyalaya.common.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull UserStatus status
) {
}
