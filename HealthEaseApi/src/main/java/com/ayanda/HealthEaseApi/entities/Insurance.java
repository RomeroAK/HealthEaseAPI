package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId")
    private Patient patient;

    // Primary insurance
    @Column(name = "provider")
    private String provider;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "group_number")
    private String groupNumber;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "copay_amount", precision = 10, scale = 2)
    private BigDecimal copayAmount;

    @Column(name = "deductible_amount", precision = 10, scale = 2)
    private BigDecimal deductibleAmount;

    // Secondary insurance
    @Column(name = "secondary_provider")
    private String secondaryProvider;

    @Column(name = "secondary_policy_number")
    private String secondaryPolicyNumber;

    @Column(name = "secondary_group_number")
    private String secondaryGroupNumber;

    @Column(name = "secondary_plan_name")
    private String secondaryPlanName;

    // South Africa specific
    @Column(name = "dependent_code")
    private String dependentCode;

    @Column(name = "authorization_required")
    private Boolean authorizationRequired = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
