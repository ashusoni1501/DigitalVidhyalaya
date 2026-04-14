package com.digitalvidhyalaya.tenant.dto;

import com.digitalvidhyalaya.common.enums.SchoolStatus;
import java.time.LocalDateTime;

public record SchoolResponse(
        Long id,
        String schoolId,
        String name,
        String code,
        SchoolStatus status,
        String addressLine1,
        String addressLine2,
        String country,
        String state,
        String district,
        String city,
        String pincode,
        String primaryPhone,
        String primaryEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
