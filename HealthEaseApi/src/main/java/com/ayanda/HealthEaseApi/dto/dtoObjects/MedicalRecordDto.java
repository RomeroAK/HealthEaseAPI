package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {

    // Patient Information
    @JsonProperty("patient_id")
    private Long patientId;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("patient_email")
    private String patientEmail;

    @JsonProperty("patient_phone")
    private String patientPhone;

    @JsonProperty("date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;

    private Integer age;

    private String gender;

    @JsonProperty("blood_group")
    private String bloodGroup;

    // Medical History Summary
    @JsonProperty("medical_history_summary")
    private String medicalHistorySummary;

    @JsonProperty("known_allergies")
    private List<String> knownAllergies;

    @JsonProperty("chronic_conditions")
    private List<String> chronicConditions;

    // Appointments
    @JsonProperty("total_appointments")
    private Integer totalAppointments;

    @JsonProperty("recent_appointments")
    private List<AppointmentHistoryDto> recentAppointments;

    @JsonProperty("upcoming_appointments")
    private List<AppointmentHistoryDto> upcomingAppointments;

    // Prescriptions
    @JsonProperty("total_prescriptions")
    private Integer totalPrescriptions;

    @JsonProperty("active_prescriptions")
    private List<PrescriptionSummaryDto> activePrescriptions;

    @JsonProperty("past_prescriptions")
    private List<PrescriptionSummaryDto> pastPrescriptions;

    // Medications
    @JsonProperty("current_medications")
    private List<MedicationDto> currentMedications;

    @JsonProperty("medication_history")
    private List<MedicationDto> medicationHistory;

    // Surgeries
    @JsonProperty("total_surgeries")
    private Integer totalSurgeries;

    @JsonProperty("surgeries")
    private List<String> surgeries;


    // Metadata
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("generated_at")
    private LocalDateTime generatedAt;
}
