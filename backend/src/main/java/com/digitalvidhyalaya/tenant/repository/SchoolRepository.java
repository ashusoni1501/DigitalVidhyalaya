package com.digitalvidhyalaya.tenant.repository;

import com.digitalvidhyalaya.common.enums.SchoolStatus;
import com.digitalvidhyalaya.tenant.entity.School;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findBySchoolId(String schoolId);

    Optional<School> findBySchoolIdAndDeletedFalse(String schoolId);

    boolean existsBySchoolId(String schoolId);

    List<School> findAllByDeletedFalseOrderByCreatedAtDesc();

    List<School> findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(SchoolStatus status);
}
