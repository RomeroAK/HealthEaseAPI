package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_education")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotBlank(message = "Institution is required")
    private String institution;

    @NotBlank(message = "Degree is required")
    private String degree;

    @NotBlank(message = "Field of study is required")
    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Min(value = 1950, message = "Start year must be after 1950")
    @Column(name = "start_year")
    private Integer startYear;

    @Min(value = 1950, message = "End year must be after 1950")
    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "is_currently_studying")
    @Builder.Default
    private Boolean isCurrentlyStudying = false;

    private Double gpa;

    private String honors;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
