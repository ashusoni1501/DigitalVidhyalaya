package com.digitalvidhyalaya.tenant.repository;

import com.digitalvidhyalaya.tenant.entity.SchoolSetting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolSettingRepository extends JpaRepository<SchoolSetting, Long> {

    Optional<SchoolSetting> findBySchool_SchoolId(String schoolId);
}
