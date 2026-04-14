package com.digitalvidhyalaya.user.entity;

import com.digitalvidhyalaya.common.entity.BaseEntity;
import com.digitalvidhyalaya.common.enums.RoleCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_code", columnNames = "code")
        }
)
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoleCode code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;
}
