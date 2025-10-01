package com.ayanda.HealthEaseApi.dto.dtoObjects;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCompletionRequest {

    @NotBlank(message = "Consultation notes are required")
    private String consultationNotes;

    private String diagnosis;

    private String treatmentPlan;

    @Builder.Default
    private Boolean followUpRequired = false;

    private LocalDate followUpDate;

    private String prescriptions; // JSON string or comma-separated list

    private String labOrdersRequired; // any lab tests to be ordered

    private String referrals; // referrals to other specialists

    private String patientInstructions; // post-consultation instructions

    private Integer severity; // 1-10 scale

    @Builder.Default
    private Boolean emergencyReferral = false;

    private String nextSteps; // what patient should do next
}
