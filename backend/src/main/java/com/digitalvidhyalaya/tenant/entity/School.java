package com.digitalvidhyalaya.tenant.entity;

import com.digitalvidhyalaya.common.entity.SoftDeletableEntity;
import com.digitalvidhyalaya.common.enums.SchoolStatus;
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
        name = "schools",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_school_school_id", columnNames = "school_id")
        }
)
public class School extends SoftDeletableEntity {

    @Column(name = "school_id", nullable = false, length = 50)
    private String schoolId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 30)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SchoolStatus status;

    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Column(nullable = false, length = 100)
    private String country = "India";

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 12)
    private String pincode;

    @Column(name = "primary_phone", length = 20)
    private String primaryPhone;

    @Column(name = "primary_email", length = 150)
    private String primaryEmail;
}
