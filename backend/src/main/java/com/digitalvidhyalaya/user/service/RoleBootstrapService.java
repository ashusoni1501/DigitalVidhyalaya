package com.digitalvidhyalaya.user.service;

import com.digitalvidhyalaya.common.enums.RoleCode;
import com.digitalvidhyalaya.user.entity.Role;
import com.digitalvidhyalaya.user.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Keeps the core platform roles available without requiring manual inserts
 * before the first user-management or login tests.
 */
@Service
public class RoleBootstrapService {

    private final RoleRepository roleRepository;

    public RoleBootstrapService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeRoles() {
        Map<RoleCode, String> descriptions = Map.of(
                RoleCode.SUPER_ADMIN, "Platform owner role",
                RoleCode.ADMIN, "School administrator role",
                RoleCode.STAFF, "Operational staff role",
                RoleCode.TEACHER, "Teacher role"
        );

        for (RoleCode roleCode : RoleCode.values()) {
            roleRepository.findByCode(roleCode)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setCode(roleCode);
                        role.setName(roleCode.name());
                        role.setDescription(descriptions.get(roleCode));
                        return roleRepository.save(role);
                    });
        }
    }
}
