package com.digitalvidhyalaya.tenant.dto;

import java.time.LocalDateTime;

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
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
