package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalHistoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Patient patient;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "height")
    private Integer height; // in centimeters

    @Column(name = "weight")
    private Integer weight; // in kilograms

    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies; // Comma-separated list

    @Column(name = "chronic_conditions", columnDefinition = "TEXT")
    private String chronicConditions; // Comma-separated list

    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications; // Comma-separated list

    @Column(name = "previous_surgeries", columnDefinition = "TEXT")
    private String previousSurgeries; // Comma-separated list

    @Column(name = "family_medical_history", columnDefinition = "TEXT")
    private String familyMedicalHistory;

    @Column(name = "smoking_status")
    private String smokingStatus;

    @Column(name = "alcohol_consumption")
    private String alcoholConsumption;

    @Column(name = "exercise_frequency")
    private String exerciseFrequency;

    @Column(name = "dietary_restrictions")
    private String dietaryRestrictions;

    @Column(name = "vaccination_status", columnDefinition = "TEXT")
    private String vaccinationStatus; // Comma-separated list

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
