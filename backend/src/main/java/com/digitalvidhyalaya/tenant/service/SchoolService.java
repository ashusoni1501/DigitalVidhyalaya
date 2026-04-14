package com.digitalvidhyalaya.tenant.service;

import com.digitalvidhyalaya.common.enums.SchoolStatus;
import com.digitalvidhyalaya.common.exception.ResourceNotFoundException;
import com.digitalvidhyalaya.tenant.dto.CreateSchoolRequest;
import com.digitalvidhyalaya.tenant.dto.SchoolResponse;
import com.digitalvidhyalaya.tenant.dto.UpdateSchoolRequest;
import com.digitalvidhyalaya.tenant.entity.School;
import com.digitalvidhyalaya.tenant.repository.SchoolRepository;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolResponse createSchool(CreateSchoolRequest request) {
        School school = new School();
        school.setSchoolId(generateSchoolId(request.name(), request.state(), request.city()));
        school.setStatus(SchoolStatus.ACTIVE);
        applySchoolFields(school, request);
        return toResponse(schoolRepository.save(school));
    }

    @Transactional(readOnly = true)
    public List<SchoolResponse> getSchools(SchoolStatus status) {
        List<School> schools = status == null
                ? schoolRepository.findAllByDeletedFalseOrderByCreatedAtDesc()
                : schoolRepository.findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(status);
        return schools.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SchoolResponse getSchoolBySchoolId(String schoolId) {
        return toResponse(findSchoolBySchoolId(schoolId));
    }

    public SchoolResponse updateSchool(String schoolId, UpdateSchoolRequest request) {
        School school = findSchoolBySchoolId(schoolId);
        applySchoolFields(school, request);
        return toResponse(schoolRepository.save(school));
    }

    public SchoolResponse suspendSchool(String schoolId) {
        School school = findSchoolBySchoolId(schoolId);
        school.setStatus(SchoolStatus.SUSPENDED);
        return toResponse(schoolRepository.save(school));
    }

    public SchoolResponse deactivateSchool(String schoolId) {
        School school = findSchoolBySchoolId(schoolId);
        school.setStatus(SchoolStatus.DEACTIVATED);
        return toResponse(schoolRepository.save(school));
    }

    private School findSchoolBySchoolId(String schoolId) {
        return schoolRepository.findBySchoolIdAndDeletedFalse(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found for schoolId: " + schoolId));
    }

    private void applySchoolFields(School school, CreateSchoolRequest request) {
        school.setName(request.name().trim());
        school.setCode(normalizeBlank(request.code()));
        school.setAddressLine1(normalizeBlank(request.addressLine1()));
        school.setAddressLine2(normalizeBlank(request.addressLine2()));
        school.setCountry(normalizeCountry(request.country()));
        school.setState(request.state().trim());
        school.setDistrict(request.district().trim());
        school.setCity(request.city().trim());
        school.setPincode(request.pincode().trim());
        school.setPrimaryPhone(normalizeBlank(request.primaryPhone()));
        school.setPrimaryEmail(normalizeBlank(request.primaryEmail()));
    }

    private void applySchoolFields(School school, UpdateSchoolRequest request) {
        school.setName(request.name().trim());
        school.setCode(normalizeBlank(request.code()));
        school.setAddressLine1(normalizeBlank(request.addressLine1()));
        school.setAddressLine2(normalizeBlank(request.addressLine2()));
        school.setCountry(normalizeCountry(request.country()));
        school.setState(request.state().trim());
        school.setDistrict(request.district().trim());
        school.setCity(request.city().trim());
        school.setPincode(request.pincode().trim());
        school.setPrimaryPhone(normalizeBlank(request.primaryPhone()));
        school.setPrimaryEmail(normalizeBlank(request.primaryEmail()));
    }

    private String normalizeCountry(String country) {
        String normalized = normalizeBlank(country);
        return normalized == null ? "India" : normalized;
    }

    private String normalizeBlank(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String generateSchoolId(String schoolName, String state, String city) {
        String prefix = buildToken(state, 3) + buildToken(city, 3) + buildToken(schoolName, 4);
        int sequence = 1;
        String candidate;

        do {
            candidate = "DV-" + prefix + "-" + String.format("%03d", sequence++);
        } while (schoolRepository.existsBySchoolId(candidate));

        return candidate;
    }

    private String buildToken(String value, int maxLength) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase(Locale.ROOT);

        if (normalized.isBlank()) {
            return "X".repeat(maxLength);
        }

        return normalized.substring(0, Math.min(normalized.length(), maxLength));
    }

    private SchoolResponse toResponse(School school) {
        return new SchoolResponse(
                school.getId(),
                school.getSchoolId(),
                school.getName(),
                school.getCode(),
                school.getStatus(),
                school.getAddressLine1(),
                school.getAddressLine2(),
                school.getCountry(),
                school.getState(),
                school.getDistrict(),
                school.getCity(),
                school.getPincode(),
                school.getPrimaryPhone(),
                school.getPrimaryEmail(),
                school.getCreatedAt(),
                school.getUpdatedAt()
        );
    }
}
