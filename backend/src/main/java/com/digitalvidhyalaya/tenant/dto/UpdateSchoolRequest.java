package com.digitalvidhyalaya.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSchoolRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 30) String code,
        @Size(max = 255) String addressLine1,
        @Size(max = 255) String addressLine2,
        @Size(max = 100) String country,
        @NotBlank @Size(max = 100) String state,
        @NotBlank @Size(max = 100) String district,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 12) String pincode,
        @Size(max = 20) String primaryPhone,
        @Email @Size(max = 150) String primaryEmail
) {
}
