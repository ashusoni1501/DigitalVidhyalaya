package com.digitalvidhyalaya.tenant.controller;

import com.digitalvidhyalaya.common.enums.SchoolStatus;
import com.digitalvidhyalaya.common.response.ApiResponse;
import com.digitalvidhyalaya.tenant.dto.CreateSchoolRequest;
import com.digitalvidhyalaya.tenant.dto.SchoolResponse;
import com.digitalvidhyalaya.tenant.dto.UpdateSchoolRequest;
import com.digitalvidhyalaya.tenant.service.SchoolService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping
    public ApiResponse<SchoolResponse> createSchool(@Valid @RequestBody CreateSchoolRequest request) {
        return ApiResponse.success("School created successfully", schoolService.createSchool(request));
    }

    @GetMapping
    public ApiResponse<List<SchoolResponse>> getSchools(@RequestParam(required = false) SchoolStatus status) {
        return ApiResponse.success("Schools fetched successfully", schoolService.getSchools(status));
    }

    @GetMapping("/{schoolId}")
    public ApiResponse<SchoolResponse> getSchool(@PathVariable String schoolId) {
        return ApiResponse.success("School fetched successfully", schoolService.getSchoolBySchoolId(schoolId));
    }

    @PutMapping("/{schoolId}")
    public ApiResponse<SchoolResponse> updateSchool(
            @PathVariable String schoolId,
            @Valid @RequestBody UpdateSchoolRequest request
    ) {
        return ApiResponse.success("School updated successfully", schoolService.updateSchool(schoolId, request));
    }

    @PatchMapping("/{schoolId}/suspend")
    public ApiResponse<SchoolResponse> suspendSchool(@PathVariable String schoolId) {
        return ApiResponse.success("School suspended successfully", schoolService.suspendSchool(schoolId));
    }

    @PatchMapping("/{schoolId}/deactivate")
    public ApiResponse<SchoolResponse> deactivateSchool(@PathVariable String schoolId) {
        return ApiResponse.success("School deactivated successfully", schoolService.deactivateSchool(schoolId));
    }
}
