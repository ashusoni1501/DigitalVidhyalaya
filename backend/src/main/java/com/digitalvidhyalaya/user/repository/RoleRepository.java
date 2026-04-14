package com.digitalvidhyalaya.user.repository;

import com.digitalvidhyalaya.common.enums.RoleCode;
import com.digitalvidhyalaya.user.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCode(RoleCode code);
}
