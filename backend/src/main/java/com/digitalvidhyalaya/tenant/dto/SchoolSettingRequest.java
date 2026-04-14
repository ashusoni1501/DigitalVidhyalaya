package com.digitalvidhyalaya.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SchoolSettingRequest(
        boolean requireAdmissionApproval,
        boolean requireStudentEditApproval,
        boolean allowCashPayment,
        boolean allowUpiPayment,
        @NotBlank @Size(max = 20) String receiptPrefix,
        @NotBlank @Size(max = 20) String admissionPrefix,
        @NotBlank @Size(max = 100) String defaultCountry
) {
}
