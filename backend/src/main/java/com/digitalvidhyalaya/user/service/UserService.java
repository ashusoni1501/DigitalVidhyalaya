package com.digitalvidhyalaya.user.service;

import com.digitalvidhyalaya.common.enums.RecordStatus;
import com.digitalvidhyalaya.common.enums.RoleCode;
import com.digitalvidhyalaya.common.enums.UserStatus;
import com.digitalvidhyalaya.common.exception.ResourceNotFoundException;
import com.digitalvidhyalaya.tenant.entity.School;
import com.digitalvidhyalaya.tenant.repository.SchoolRepository;
import com.digitalvidhyalaya.user.dto.CreateUserRequest;
import com.digitalvidhyalaya.user.dto.UpdateUserStatusRequest;
import com.digitalvidhyalaya.user.dto.UserResponse;
import com.digitalvidhyalaya.user.entity.Role;
import com.digitalvidhyalaya.user.entity.User;
import com.digitalvidhyalaya.user.entity.UserSchoolRole;
import com.digitalvidhyalaya.user.repository.RoleRepository;
import com.digitalvidhyalaya.user.repository.UserRepository;
import com.digitalvidhyalaya.user.repository.UserSchoolRoleRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;
    private final UserSchoolRoleRepository userSchoolRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            SchoolRepository schoolRepository,
            UserSchoolRoleRepository userSchoolRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.schoolRepository = schoolRepository;
        this.userSchoolRoleRepository = userSchoolRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username().trim())) {
            throw new IllegalArgumentException("Username already exists");
        }

        School school = findSchool(request.schoolId());

        User user = new User();
        user.setUsername(request.username().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName().trim());
        user.setEmail(normalizeBlank(request.email()));
        user.setPhone(normalizeBlank(request.phone()));
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        assignRoles(user, school, request.roles());
        return toResponse(user, school.getSchoolId(), request.roles());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersBySchool(String schoolId) {
        findSchool(schoolId);

        List<UserSchoolRole> mappings = userSchoolRoleRepository
                .findAllBySchool_SchoolIdAndStatus(schoolId, RecordStatus.ACTIVE);

        Map<Long, List<UserSchoolRole>> userMappings = mappings.stream()
                .collect(Collectors.groupingBy(mapping -> mapping.getUser().getId()));

        return userMappings.values().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + userId));
        user.setStatus(request.status());
        user = userRepository.save(user);

        List<UserSchoolRole> mappings = userSchoolRoleRepository.findAllByUser_IdAndStatus(userId, RecordStatus.ACTIVE);
        if (mappings.isEmpty()) {
            throw new IllegalArgumentException("User has no active school role assignments");
        }

        return toResponse(mappings);
    }

    private void assignRoles(User user, School school, Set<RoleCode> roleCodes) {
        for (RoleCode roleCode : roleCodes) {
            Role role = roleRepository.findByCode(roleCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found for code: " + roleCode));

            UserSchoolRole mapping = new UserSchoolRole();
            mapping.setUser(user);
            mapping.setSchool(school);
            mapping.setRole(role);
            mapping.setStatus(RecordStatus.ACTIVE);
            mapping.setAssignedAt(OffsetDateTime.now());
            mapping.setAssignedBy("system");
            userSchoolRoleRepository.save(mapping);
        }
    }

    private School findSchool(String schoolId) {
        return schoolRepository.findBySchoolIdAndDeletedFalse(schoolId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("School not found for schoolId: " + schoolId));
    }

    private UserResponse toResponse(List<UserSchoolRole> mappings) {
        User user = mappings.getFirst().getUser();
        String schoolId = mappings.getFirst().getSchool().getSchoolId();
        Set<RoleCode> roles = mappings.stream()
                .map(mapping -> mapping.getRole().getCode())
                .collect(Collectors.toSet());
        return toResponse(user, schoolId, roles);
    }

    private UserResponse toResponse(User user, String schoolId, Set<RoleCode> roles) {
        return new UserResponse(
                user.getId(),
                schoolId,
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                roles,
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private String normalizeBlank(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
