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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_school_class_section", columnNames = {"school_id", "class_id", "name"})
        }
)
public class Section extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecordStatus status;
}
