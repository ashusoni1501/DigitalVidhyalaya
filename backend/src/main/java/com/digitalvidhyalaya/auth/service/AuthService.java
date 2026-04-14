package com.digitalvidhyalaya.auth.service;

import com.digitalvidhyalaya.auth.dto.LoginRequest;
import com.digitalvidhyalaya.auth.dto.LoginResponse;
import com.digitalvidhyalaya.auth.security.AuthenticatedUser;
import com.digitalvidhyalaya.common.enums.RecordStatus;
import com.digitalvidhyalaya.common.enums.RoleCode;
import com.digitalvidhyalaya.common.enums.SchoolStatus;
import com.digitalvidhyalaya.common.enums.UserStatus;
import com.digitalvidhyalaya.common.exception.ResourceNotFoundException;
import com.digitalvidhyalaya.tenant.entity.School;
import com.digitalvidhyalaya.tenant.repository.SchoolRepository;
import com.digitalvidhyalaya.user.entity.User;
import com.digitalvidhyalaya.user.entity.UserSchoolRole;
import com.digitalvidhyalaya.user.repository.UserRepository;
import com.digitalvidhyalaya.user.repository.UserSchoolRoleRepository;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final UserSchoolRoleRepository userSchoolRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            SchoolRepository schoolRepository,
            UserRepository userRepository,
            UserSchoolRoleRepository userSchoolRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
        this.userSchoolRoleRepository = userSchoolRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        AuthenticatedUser authenticatedUser = authenticate(request);
        return new LoginResponse(
                authenticatedUser.schoolId(),
                authenticatedUser.username(),
                authenticatedUser.fullName(),
                authenticatedUser.roles(),
                authenticatedUser.readOnlyAccess(),
                authenticatedUser.readOnlyAccess()
                        ? "Login successful with read-only access"
                        : "Login successful"
        );
    }

    public AuthenticatedUser authenticate(LoginRequest request) {
        School school = schoolRepository.findBySchoolIdAndDeletedFalse(request.schoolId().trim())
                .orElseThrow(() -> new ResourceNotFoundException("School not found for schoolId: " + request.schoolId()));

        User user = userRepository.findByUsernameAndDeletedFalse(request.username().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User account is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Set<UserSchoolRole> activeRoleMappings = userSchoolRoleRepository
                .findAllByUser_IdAndSchool_IdAndStatus(user.getId(), school.getId(), RecordStatus.ACTIVE);

        if (activeRoleMappings.isEmpty()) {
            throw new IllegalArgumentException("User is not assigned to this school");
        }

        Set<RoleCode> roles = activeRoleMappings.stream()
                .map(mapping -> mapping.getRole().getCode())
                .collect(Collectors.toSet());

        validateSchoolAccess(school, roles);

        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        boolean readOnlyAccess = school.getStatus() == SchoolStatus.SUSPENDED;

        return new AuthenticatedUser(
                user.getId(),
                school.getId(),
                school.getSchoolId(),
                user.getUsername(),
                user.getFullName(),
                roles,
                readOnlyAccess
        );
    }

    private void validateSchoolAccess(School school, Set<RoleCode> roles) {
        if (school.getStatus() == SchoolStatus.DEACTIVATED) {
            throw new IllegalArgumentException("This school is deactivated and login is not allowed");
        }

        if (school.getStatus() == SchoolStatus.SUSPENDED && !roles.contains(RoleCode.ADMIN)) {
            throw new IllegalArgumentException(
                    "Your school account is currently suspended. Please contact your school administrator."
            );
        }
    }
}
