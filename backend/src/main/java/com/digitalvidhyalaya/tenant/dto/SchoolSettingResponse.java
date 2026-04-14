package com.digitalvidhyalaya.tenant.dto;

import java.time.OffsetDateTime;

public record SchoolSettingResponse(
        Long id,
        String schoolId,
        boolean requireAdmissionApproval,
        boolean requireStudentEditApproval,
        boolean allowCashPayment,
        boolean allowUpiPayment,
        String receiptPrefix,
        String admissionPrefix,
        String defaultCountry,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
