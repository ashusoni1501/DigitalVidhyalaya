package com.digitalvidhyalaya.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SoftDeletableEntity extends AuditableBaseEntity {

    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    private String deletedBy;
}
