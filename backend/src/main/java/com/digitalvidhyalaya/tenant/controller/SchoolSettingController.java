package com.digitalvidhyalaya.tenant.controller;

import com.digitalvidhyalaya.common.response.ApiResponse;
import com.digitalvidhyalaya.tenant.dto.SchoolSettingRequest;
import com.digitalvidhyalaya.tenant.dto.SchoolSettingResponse;
import com.digitalvidhyalaya.tenant.service.SchoolSettingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants/schools/{schoolId}/settings")
public class SchoolSettingController {

    private final SchoolSettingService schoolSettingService;

    public SchoolSettingController(SchoolSettingService schoolSettingService) {
        this.schoolSettingService = schoolSettingService;
    }

    @GetMapping
    public ApiResponse<SchoolSettingResponse> getSettings(@PathVariable String schoolId) {
        return ApiResponse.success(
                "School settings fetched successfully",
                schoolSettingService.getSettings(schoolId)
        );
    }

    @PutMapping
    public ApiResponse<SchoolSettingResponse> upsertSettings(
            @PathVariable String schoolId,
            @Valid @RequestBody SchoolSettingRequest request
    ) {
        return ApiResponse.success(
                "School settings saved successfully",
                schoolSettingService.upsertSettings(schoolId, request)
        );
    }
}
