package com.digitalvidhyalaya.tenant.service;

import com.digitalvidhyalaya.common.exception.ResourceNotFoundException;
import com.digitalvidhyalaya.tenant.dto.SchoolSettingRequest;
import com.digitalvidhyalaya.tenant.dto.SchoolSettingResponse;
import com.digitalvidhyalaya.tenant.entity.School;
import com.digitalvidhyalaya.tenant.entity.SchoolSetting;
import com.digitalvidhyalaya.tenant.repository.SchoolRepository;
import com.digitalvidhyalaya.tenant.repository.SchoolSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolSettingService {

    private final SchoolRepository schoolRepository;
    private final SchoolSettingRepository schoolSettingRepository;

    public SchoolSettingService(
            SchoolRepository schoolRepository,
            SchoolSettingRepository schoolSettingRepository
    ) {
        this.schoolRepository = schoolRepository;
        this.schoolSettingRepository = schoolSettingRepository;
    }

    @Transactional(readOnly = true)
    public SchoolSettingResponse getSettings(String schoolId) {
        return toResponse(findOrCreateSchoolSetting(findSchoolBySchoolId(schoolId)));
    }

    public SchoolSettingResponse upsertSettings(String schoolId, SchoolSettingRequest request) {
        if (!request.allowCashPayment() && !request.allowUpiPayment()) {
            throw new IllegalArgumentException("At least one payment mode must be enabled");
        }

        School school = findSchoolBySchoolId(schoolId);
        SchoolSetting schoolSetting = findOrCreateSchoolSetting(school);

        schoolSetting.setRequireAdmissionApproval(request.requireAdmissionApproval());
        schoolSetting.setRequireStudentEditApproval(request.requireStudentEditApproval());
        schoolSetting.setAllowCashPayment(request.allowCashPayment());
        schoolSetting.setAllowUpiPayment(request.allowUpiPayment());
        schoolSetting.setReceiptPrefix(request.receiptPrefix().trim().toUpperCase());
        schoolSetting.setAdmissionPrefix(request.admissionPrefix().trim().toUpperCase());
        schoolSetting.setDefaultCountry(request.defaultCountry().trim());

        return toResponse(schoolSettingRepository.save(schoolSetting));
    }

    private School findSchoolBySchoolId(String schoolId) {
        return schoolRepository.findBySchoolIdAndDeletedFalse(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found for schoolId: " + schoolId));
    }

    private SchoolSetting findOrCreateSchoolSetting(School school) {
        return schoolSettingRepository.findBySchool_SchoolId(school.getSchoolId())
                .orElseGet(() -> {
                    SchoolSetting schoolSetting = new SchoolSetting();
                    schoolSetting.setSchool(school);
                    schoolSetting.setDefaultCountry(school.getCountry());
                    return schoolSettingRepository.save(schoolSetting);
                });
    }

    private SchoolSettingResponse toResponse(SchoolSetting schoolSetting) {
        return new SchoolSettingResponse(
                schoolSetting.getId(),
                schoolSetting.getSchool().getSchoolId(),
                schoolSetting.isRequireAdmissionApproval(),
                schoolSetting.isRequireStudentEditApproval(),
                schoolSetting.isAllowCashPayment(),
                schoolSetting.isAllowUpiPayment(),
                schoolSetting.getReceiptPrefix(),
                schoolSetting.getAdmissionPrefix(),
                schoolSetting.getDefaultCountry(),
                schoolSetting.getCreatedAt(),
                schoolSetting.getUpdatedAt()
        );
    }
}
