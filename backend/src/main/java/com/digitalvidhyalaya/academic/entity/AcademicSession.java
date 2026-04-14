package com.digitalvidhyalaya.academic.entity;

import com.digitalvidhyalaya.common.entity.SoftDeletableEntity;
import com.digitalvidhyalaya.common.enums.RecordStatus;
import com.digitalvidhyalaya.tenant.entity.School;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "academic_sessions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_school_session_name", columnNames = {"school_id", "name"})
        }
)
public class AcademicSession extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_current", nullable = false)
    private boolean current;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecordStatus status;
}
