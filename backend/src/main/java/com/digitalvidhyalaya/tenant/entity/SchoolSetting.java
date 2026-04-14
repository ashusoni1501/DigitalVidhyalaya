package com.digitalvidhyalaya.tenant.entity;

import com.digitalvidhyalaya.common.entity.AuditableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "school_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_school_settings_school", columnNames = "school_id")
        }
)
public class SchoolSetting extends AuditableBaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "require_admission_approval", nullable = false)
    private boolean requireAdmissionApproval = true;

    @Column(name = "require_student_edit_approval", nullable = false)
    private boolean requireStudentEditApproval = true;

    @Column(name = "allow_cash_payment", nullable = false)
    private boolean allowCashPayment = true;

    @Column(name = "allow_upi_payment", nullable = false)
    private boolean allowUpiPayment = true;

    @Column(name = "receipt_prefix", nullable = false, length = 20)
    private String receiptPrefix = "RCPT";

    @Column(name = "admission_prefix", nullable = false, length = 20)
    private String admissionPrefix = "ADM";

    @Column(name = "default_country", nullable = false, length = 100)
    private String defaultCountry = "India";
}
