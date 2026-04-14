package com.digitalvidhyalaya.user.repository;

import com.digitalvidhyalaya.common.enums.RecordStatus;
import com.digitalvidhyalaya.user.entity.UserSchoolRole;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSchoolRoleRepository extends JpaRepository<UserSchoolRole, Long> {

    Set<UserSchoolRole> findAllByUser_IdAndSchool_IdAndStatus(Long userId, Long schoolId, RecordStatus status);
}
