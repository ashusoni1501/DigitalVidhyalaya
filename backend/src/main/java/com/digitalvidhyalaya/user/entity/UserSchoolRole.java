package com.digitalvidhyalaya.user.entity;

import com.digitalvidhyalaya.common.entity.AuditableBaseEntity;
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
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "user_school_roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_school_role",
                        columnNames = {"user_id", "school_id", "role_id"}
                )
        }
)
public class UserSchoolRole extends AuditableBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecordStatus status;

    @Column(name = "assigned_at", nullable = false)
    private OffsetDateTime assignedAt;

    @Column(name = "assigned_by", length = 100)
    private String assignedBy;
}
